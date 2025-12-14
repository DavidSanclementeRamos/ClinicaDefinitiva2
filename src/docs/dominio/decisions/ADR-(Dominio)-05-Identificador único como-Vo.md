# ADR-26 (Dominio): Identificador único de agregados como String encapsulado en VO

- Estado: Aprobado
- Fecha: 2025-10-24
- Autor: David

## Contexto
En el diseño del dominio, cada agregado requiere un identificador único.  
Se evaluaron dos opciones principales:
- Usar long autoincremental (eficiente en BD relacionales, pero limitado en integraciones).
- Usar string (UUID/ULID) encapsulado en un Value Object (VO), más flexible y trazable.

## Decisión
Se utilizará string como identificador único, encapsulado en un Value Object específico para cada agregado.  
Ejemplo: PatientId, InvoiceId, ServiceId.  
La generación se hará mediante UUID/ULID, garantizando unicidad global y evitando dependencia de la base de datos.

## Consecuencias
- ✅ Mayor trazabilidad y auditabilidad en contextos clínicos y regulatorios.
- ✅ Independencia de la estrategia de persistencia.
- ✅ Flexibilidad para integraciones con EPS, DIAN y otros sistemas externos.
- ⚠ Mayor costo en almacenamiento e índices comparado con enteros.
- ⚠ IDs menos legibles para humanos, aunque mitigable con prefijos semánticos.

## Ejemplo de implementación
```java
public final class PatientId {
private final String value;

    private PatientId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static PatientId generate() {
        return new PatientId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }
}

public final class Patient {
private final PatientId id;
private final PersonInfo info;
// atributos específicos de paciente
}
```

## Plan de implementación
1. Crear VO de ID para cada agregado (PatientId, DentistId, InvoiceId, ServiceId).
2. Definir métodos generate() basados en UUID/ULID.
3. Integrar IDs en repositorios y servicios de aplicación.
4. Documentar convención de prefijos semánticos (ej. PAT-UUID, INV-UUID).
5. Añadir pruebas unitarias para generación, igualdad y trazabilidad de IDs.

## Relación con otros ADR
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico.
- ADR-23 (Dominio): Desactivación de usuario debe ser responsabilidad del agregado UserModel.
- ADR-11 (Dominio): Uso meticuloso de excepciones personalizadas.  
  

