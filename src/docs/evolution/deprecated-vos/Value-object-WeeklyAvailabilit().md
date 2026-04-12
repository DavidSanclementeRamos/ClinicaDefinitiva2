# Value Object: WeeklyAvailability

## Propósito

WeeklyAvailability encapsula la disponibilidad semanal de un odontólogo como una colección validada de jornadas (Schedule). Representa la planificación clínica recurrente del profesional, permitiendo validar coherencia, evitar solapamientos y delegar la lógica de generación de disponibilidades al dominio.

Este VO evita el uso de listas dispersas de días y horarios sin validación, y permite construir una estructura trazable, exhibible y ética para la atención semanal.

## Motivación

La disponibilidad semanal es un dato clínico operativo que afecta la generación de slots, la asignación de turnos, la trazabilidad de atención y la planificación de recursos. Usar listas de Schedule sin encapsulación genera ambigüedad, errores de validación y degeneración semántica. Este VO permite representar la semana como una entidad coherente, con validación, formato y proyección.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class WeeklyAvailability {

    private final List<Schedule> schedules;

    public WeeklyAvailability(List<Schedule> schedules) {
        if (schedules == null || schedules.isEmpty()) {
            throw new ClinicalValidationException("La disponibilidad semanal no puede estar vacía");
        }

        for (int i = 0; i < schedules.size(); i++) {
            for (int j = i + 1; j < schedules.size(); j++) {
                if (schedules.get(i).overlapsWith(schedules.get(j))) {
                    throw new ClinicalValidationException("Las jornadas semanales no pueden solaparse");
                }
            }
        }

        this.schedules = List.copyOf(schedules);
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public List<Schedule> getSchedulesForDay(DayOfWeek day) {
        return schedules.stream()
            .filter(s -> s.getDay().equals(day))
            .collect(Collectors.toList());
    }

    public boolean isAvailable(DayOfWeek day, LocalTime time) {
        return getSchedulesForDay(day).stream()
            .anyMatch(s -> s.includes(time));
    }
}
```
## Reglas clínicas encapsuladas

- La disponibilidad semanal no puede estar vacía.
- Las jornadas (Schedule) no deben solaparse.
- Permite consultar disponibilidad por día y hora.
- Permite agrupar jornadas por día para planificación.

## Uso en el modelo

- Odontólogo → usa WeeklyAvailability como parte de su perfil clínico.
- Disponibilidad → se genera a partir de WeeklyAvailability.
- AgendaService, TurnoService → consultan WeeklyAvailability para validar asignación.
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

- Métodos para generar slots clínicos por semana.
- Soporte para semanas excepcionales o discontinuas.
- Integración con VO AvailabilityStatus y TurnDuration.
- Posibilidad de representar restricciones clínicas (pausas, urgencias, feriados).

# Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  `