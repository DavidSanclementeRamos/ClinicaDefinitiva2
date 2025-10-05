# Value Object: WorkingHours

## Propósito

WorkingHours encapsula el rango horario de atención de un odontólogo como una unidad semántica validada. Representa el intervalo entre hora de inicio y hora de fin, permitiendo validar coherencia, generar slots clínicos, y delegar la lógica de disponibilidad al dominio.

Este VO evita el uso de pares de LocalTime dispersos que no expresan intención clínica, y permite construir jornadas trazables, eficientes y éticamente justificadas.

## Motivación

El rango horario es un dato clínico operativo que afecta la generación de disponibilidades, la asignación de turnos, la planificación de jornadas y la trazabilidad de atención. Usar LocalTime sin encapsulación genera ambigüedad, errores de validación y degeneración semántica. Este VO permite representar jornadas como entidades semánticas, con validación, formato y proyección.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class WorkingHours {
  private final LocalTime start;
  private final LocalTime end;
  private final DayOfWeek dayOfWeek;

  public WorkingHours(LocalTime start, LocalTime end, DayOfWeek dayOfWeek) {
    if (start == null || end == null ||dayOfWeek == null) {
      throw new NullWorkingHoursException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
    }
    if( !start.isBefore(end) ){
      throw new StartTimeAfterEndTimeException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
    }
    this.start = start;
    this.end = end;
    this.dayOfWeek = dayOfWeek;
  }
  public boolean isWithin(LocalDateTime dateTime) {
    if (dateTime == null) return false;
    return dateTime.getDayOfWeek().equals(dayOfWeek)
            && !dateTime.toLocalTime().isBefore(start)
            && !dateTime.toLocalTime().isAfter(end);
  }

  public boolean cubre(TimeSlot slot) {
    if (slot == null) return false;
    if (!slot.getDayOfWeek().equals(this.dayOfWeek)) return false;
    return !slot.getInicio().isBefore(start) && !slot.getFin().isAfter(end);
  }

  public Duration duracionTotal() {
    return Duration.between(start, end);
  }

  public DayOfWeek getDayOfWeek() { return dayOfWeek; }
  public LocalTime getStart() { return start; }
  public LocalTime getEnd() { return end; }
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
