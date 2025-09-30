# Value Object: AppointmentStatus

## Propósito

AppointmentStatus encapsula el estado clínico de una cita odontológica como una unidad semántica validada. Representa la fase operativa y clínica en la que se encuentra una cita, permitiendo validar transiciones, justificar acciones y evitar ambigüedad en flujos de atención.

Este VO evita el uso de cadenas o enums técnicos dispersos que no expresan intención clínica, y permite delegar la lógica de estado y trazabilidad al dominio.

## Motivación

El estado de una cita afecta directamente la atención clínica, la facturación, la trazabilidad operativa y la generación de métricas. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de transición y degeneración semántica. Este VO permite representar estados como entidades éticas, coherentes y validables.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class AppointmentStatus {

    private final String value;

    public AppointmentStatus(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("El estado de la cita no puede estar vacío");
        }

        List<String> estadosValidos = List.of(
            "PROGRAMADA", "ATENDIDA", "CANCELADA", "AUSENTE", "REAGENDADA"
        );

        if (!estadosValidos.contains(value.toUpperCase())) {
            throw new ClinicalValidationException("Estado de cita inválido: " + value);
        }

        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public boolean isScheduled() {
        return "PROGRAMADA".equals(value);
    }

    public boolean isAttended() {
        return "ATENDIDA".equals(value);
    }

    public boolean isCancelled() {
        return "CANCELADA".equals(value);
    }

    public boolean isAbsent() {
        return "AUSENTE".equals(value);
    }

    public boolean isRescheduled() {
        return "REAGENDADA".equals(value);
    }
}
```
## Reglas clínicas encapsuladas

- El estado debe ser uno de los valores válidos: PROGRAMADA, ATENDIDA, CANCELADA, AUSENTE, REAGENDADA.
- Permite validar si una cita puede ser modificada, facturada o auditada.
- Permite justificar transiciones clínicas y operativas.
- Evita ambigüedad en flujos de atención y trazabilidad.

## Uso en el modelo

- Cita → usa AppointmentStatus como parte de su estado clínico.
- TurnoService, AgendaService, FacturacionService → consultan el estado para validar operaciones.
- Se utiliza en informes, métricas, auditoría y notificaciones.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad clínica del sistema.
- Evita ambigüedad en flujos de atención.
- Permite trazabilidad ética de cada cita.

## Proyección

Este VO será extendido con:

- Métodos para justificar cambios de estado (motivo, timestamp, actor).
- Integración con eventos clínicos y operativos.
- Soporte para estados personalizados por clínica.
- Posibilidad de representar transiciones válidas y restricciones.

## Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  `