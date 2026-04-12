# Value Object: AvailabilityStatus

## Propósito

AvailabilityStatus encapsula el estado operativo de una disponibilidad clínica (slot) como una unidad semántica validada. Representa si un bloque horario está habilitado para recibir turnos, ha sido reservado, cancelado, bloqueado o agotado. Este VO permite validar transiciones, justificar acciones y evitar ambigüedad en flujos de asignación.

## Motivación

El estado de una disponibilidad afecta directamente la asignación de turnos, la planificación de jornadas, la trazabilidad operativa y la generación de métricas. Usar este dato como texto plano o enum técnico genera ambigüedad, errores de transición y degeneración semántica. Este VO permite representar estados como entidades éticas, coherentes y validables.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class AvailabilityStatus {

    private final String value;

    public AvailabilityStatus(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("El estado de disponibilidad no puede estar vacío");
        }

        List<String> estadosValidos = List.of(
            "DISPONIBLE", "RESERVADA", "AGOTADA", "CANCELADA", "BLOQUEADA"
        );

        if (!estadosValidos.contains(value.toUpperCase())) {
            throw new ClinicalValidationException("Estado de disponibilidad inválido: " + value);
        }

        this.value = value.toUpperCase();
    }

    public String getValue() {
        return value;
    }

    public boolean isAvailable() {
        return "DISPONIBLE".equals(value);
    }

    public boolean isReserved() {
        return "RESERVADA".equals(value);
    }

    public boolean isExhausted() {
        return "AGOTADA".equals(value);
    }

    public boolean isCancelled() {
        return "CANCELADA".equals(value);
    }

    public boolean isBlocked() {
        return "BLOQUEADA".equals(value);
    }
}
```
## Reglas clínicas encapsuladas

- El estado debe ser uno de los valores válidos: DISPONIBLE, RESERVADA, AGOTADA, CANCELADA, BLOQUEADA.
- Permite validar si una disponibilidad puede recibir turnos (isAvailable()).
- Permite justificar bloqueos, cancelaciones y agotamientos.
- Evita ambigüedad en flujos de asignación y planificación.

## Uso en el modelo

- Disponibilidad → usa AvailabilityStatus como parte de su estado operativo.
- TurnoService, AgendaService, CitaService → consultan el estado para validar asignación.
- Se utiliza en informes, métricas, auditoría y notificaciones.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad en flujos de atención.
- Permite trazabilidad ética de cada disponibilidad.

## Proyección

Este VO será extendido con:

- Métodos para justificar cambios de estado (motivo, timestamp, actor).
- Integración con eventos clínicos y operativos.
- Soporte para estados personalizados por clínica.
- Posibilidad de representar transiciones válidas y restricciones.

## Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  