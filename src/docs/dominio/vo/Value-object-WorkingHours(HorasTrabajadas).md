# Value Object: WorkingHours

## Propósito

WorkingHours encapsula el rango horario de atención de un odontólogo como una unidad semántica validada. Representa el intervalo entre hora de inicio y hora de fin, permitiendo validar coherencia, generar slots clínicos, y delegar la lógica de disponibilidad al dominio.

Este VO evita el uso de pares de LocalTime dispersos que no expresan intención clínica, y permite construir jornadas trazables, eficientes y éticamente justificadas.

## Motivación

El rango horario es un dato clínico operativo que afecta la generación de disponibilidades, la asignación de turnos, la planificación de jornadas y la trazabilidad de atención. Usar LocalTime sin encapsulación genera ambigüedad, errores de validación y degeneración semántica. Este VO permite representar jornadas como entidades semánticas, con validación, formato y proyección.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class WorkingHours {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public WorkingHours(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new ClinicalValidationException("Las horas de inicio y fin son obligatorias");
        }
        if (!startTime.isBefore(endTime)) {
            throw new ClinicalValidationException("La hora de inicio debe ser anterior a la hora de fin");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public boolean includes(LocalTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    public boolean overlapsWith(WorkingHours other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}
```
## Reglas clínicas encapsuladas

- La hora de inicio debe ser anterior a la hora de fin.
- Se puede calcular la duración total de la jornada.
- Permite validar si una hora específica está dentro del rango (includes()).
- Permite detectar solapamientos entre jornadas (overlapsWith()).

## Uso en el modelo

- Horario → usa WorkingHours para definir la jornada clínica.
- Disponibilidad → hereda el rango horario para generar slots.
- TurnoService → valida si un turno propuesto está dentro del rango.
- AgendaService → organiza jornadas y evita solapamientos.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad en flujos de asignación.
- Permite generación dinámica de slots clínicos.

## Proyección

Este VO será extendido con:

- Métodos para dividir el rango en bloques (generateSlots()).
- Soporte para jornadas partidas o discontinuas.
- Integración con VO TurnDuration para planificación.
- Posibilidad de representar restricciones clínicas (pausas, bloqueos).

## Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  `
