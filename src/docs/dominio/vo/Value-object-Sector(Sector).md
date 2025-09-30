# Value Object: Sector

## Propósito

Sector encapsula el área administrativa o clínica en la que opera un Secretario dentro del sistema odontológico. Representa una unidad semántica que permite validar asignaciones, filtrar responsabilidades y organizar flujos operativos según el sector al que pertenece cada actor.

Este VO evita el uso de cadenas o enums dispersos que no expresan intención clínica, y permite delegar la lógica de clasificación y validación al dominio.

## Motivación

El sector define el contexto operativo de un secretario: puede estar asignado a recepción, coordinación, facturación, atención al cliente, entre otros. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de asignación y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class Sector {

    private final String value;

    public Sector(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("El sector no puede estar vacío");
        }

        List<String> sectoresValidos = List.of("RECEPCION", "FACTURACION", "COORDINACION", "ATENCION_CLIENTE");

        if (!sectoresValidos.contains(value.toUpperCase())) {
            throw new ClinicalValidationException("Sector inválido: " + value);
        }

        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public boolean isRecepcion() {
        return "RECEPCION".equals(value);
    }

    public boolean isFacturacion() {
        return "FACTURACION".equals(value);
    }

    public boolean isCoordinacion() {
        return "COORDINACION".equals(value);
    }

    public boolean isAtencionCliente() {
        return "ATENCION_CLIENTE".equals(value);
    }
}
```
## Reglas clínicas encapsuladas

- El sector debe ser uno de los valores válidos: RECEPCION, FACTURACION, COORDINACION, ATENCION_CLIENTE.
- Permite validar asignación operativa del secretario.
- Permite filtrar responsabilidades y permisos por sector.
- Evita ambigüedad en flujos administrativos.

## Uso en el modelo

- Secretario → usa Sector como parte de su perfil operativo.
- AgendaService, TurnoService, FacturacionService → pueden filtrar operaciones por sector.
- Permite trazabilidad de acciones administrativas según contexto.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad y errores de asignación.
- Permite expansión futura sin romper contratos.

## Proyección

Este VO será extendido con:

- Soporte para sectores personalizados por clínica.
- Métodos para justificar reasignaciones de sector.
- Integración con permisos y roles operativos.
- Posibilidad de representar sectores temporales o compartidos.

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  