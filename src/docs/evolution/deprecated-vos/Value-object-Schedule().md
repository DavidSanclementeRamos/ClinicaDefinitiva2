# Value Object: Schedule

## Propósito

Schedule encapsula la jornada clínica de un odontólogo como una unidad semántica validada. Representa el día de la semana y el rango horario en el que el profesional está disponible para generar disponibilidades, recibir turnos y ser auditado. Este VO permite validar coherencia, evitar solapamientos y delegar la lógica de planificación al dominio.

## Motivación

La jornada clínica es un dato operativo que afecta la generación de slots, la asignación de turnos, la trazabilidad de atención y la planificación de recursos. Usar día y horario como datos dispersos genera ambigüedad, errores de validación y degeneración semántica. Este VO permite representar jornadas como entidades éticas, coherentes y validables.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md) , que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class Schedule {

    private final DayOfWeek day;
    private final WorkingHours workingHours;

    public Schedule(DayOfWeek day, WorkingHours workingHours) {
        if (day == null || workingHours == null) {
            throw new ClinicalValidationException("El día y el rango horario son obligatorios");
        }
        this.day = day;
        this.workingHours = workingHours;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public boolean isSameDay(Schedule other) {
        return this.day.equals(other.day);
    }

    public boolean overlapsWith(Schedule other) {
        return isSameDay(other) && this.workingHours.overlapsWith(other.workingHours);
    }

    public boolean includes(LocalTime time) {
        return workingHours.includes(time);
    }
}
```
## Reglas clínicas encapsuladas

- El día y el rango horario son obligatorios.
- Permite validar si dos jornadas se solapan (overlapsWith()).
- Permite validar si una hora está dentro de la jornada (includes()).
- Permite agrupar jornadas por día para planificación.

## Uso en el modelo

- Horario → usa Schedule como unidad de jornada clínica.
- Disponibilidad → hereda Schedule para generar slots.
- AgendaService, TurnoService → consultan Schedule para validar asignación.
- Se utiliza en planificación, trazabilidad, auditoría y generación de métricas.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad en flujos de asignación.
- Permite generación dinámica de disponibilidades.

## Proyección

Este VO será extendido con:

- Métodos para agrupar jornadas por odontólogo.
- Soporte para jornadas discontinuas o excepcionales.
- Integración con VO AvailabilityStatus y TurnDuration.
- Posibilidad de representar restricciones clínicas (pausas, bloqueos, urgencias).

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  