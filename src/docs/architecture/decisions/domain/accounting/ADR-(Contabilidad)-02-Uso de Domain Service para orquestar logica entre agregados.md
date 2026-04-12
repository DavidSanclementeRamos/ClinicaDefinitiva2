
# ADR-02 (Contabilidad): Uso de Domain Services para orquestar lógica entre agregados

- Estado: Aprobado
- Fecha: 2025-12-02
- Autor: David Stiven Sanclemente


## Contexto
En nuestro sistema, los agregados raíz representan límites claros de consistencia y encapsulan sus propias invariantes internas. Sin embargo, existen reglas de negocio que requieren coordinación entre múltiples agregados. Ejemplos típicos:
- Crear un AsientoContable solo si la Company asociada está activa.
- Registrar un Pago que debe validar la existencia de una Factura en estado pendiente.
- Generar un Contrato que depende de la vigencia de una Empresa y de un Usuario autorizado.

Si intentáramos resolver estas reglas dentro de un solo agregado, caeríamos en acoplamientos indebidos y violaríamos el principio de encapsulación. Documentar un ADR por cada caso sería redundante, ya que la solución es siempre la misma: usar un servicio de dominio para orquestar la lógica entre agregados.

---

## Decisión
Adoptamos el patrón Domain Service como mecanismo estándar para:
- Orquestar reglas de negocio que involucran múltiples agregados.
- Coordinar la interacción entre repositorios de distintos agregados.
- Mantener la cohesión interna de cada agregado, evitando que uno dependa directamente del estado o comportamiento de otro.

Los Domain Services serán clases del dominio, con nombres semánticos que expresen la operación de negocio (ej. AsientoContableService, PagoService, ContratoService).  
Cada servicio recibirá los agregados necesarios como parámetros y aplicará las reglas externas antes de delegar la creación o modificación de entidades.

---

## Alternativas consideradas
1. Validar dentro de un agregado
    - Rechazada: viola los límites de agregados y genera acoplamiento.
2. Mover entidades dependientes dentro de un mismo agregado
    - Rechazada: produce agregados demasiado grandes, difíciles de escalar y mantener.
3. Application Services como único lugar de coordinación
    - Parcialmente válido, pero preferimos Domain Services para mantener la semántica y reutilización de reglas de negocio en el dominio mismo.

---

## Consecuencias
- Positivas:
    - Claridad arquitectónica: cada agregado mantiene sus invariantes internas, y las reglas externas se concentran en servicios de dominio.
    - Escalabilidad: repositorios separados, consultas eficientes.
    - Reutilización: las reglas de negocio inter-agregados pueden invocarse desde distintos casos de uso.
    - Documentación más limpia: este ADR general se referencia en lugar de repetir la misma justificación en cada agregado.
- Negativas:
    - Introduce una capa adicional de orquestación.
    - Requiere disciplina para mantener los servicios del dominio libres de lógica de infraestructura.

---

Ejemplo de implementación
```java
public class AsientoContableService {
private final CompanyRepository companyRepo;
private final AsientoContableRepository asientoRepo;

    public AsientoContable crearAsiento(CompanyId companyId, DatosAsiento datos) {
        Company company = companyRepo.findById(companyId)
            .orElseThrow(() -> new DomainException("Empresa no encontrada"));

        if (!company.getStatus().isActive()) {
            throw new DomainException("Empresa inactiva: no se puede crear asiento");
        }

        AsientoContable asiento = AsientoContable.crear(companyId, datos);
        return asientoRepo.save(asiento);
    }
}
```
Este ADR se convierte en referencia general para todos los casos donde un agregado dependa de lógica externa. Los ADRs específicos solo mencionarán: “La validación inter-agregados se implementa mediante Domain Services, según ADR-XXX”.

---

