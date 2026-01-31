# ADR-10 (Dominio): Modelo temporal en dominio clínico

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Dominio

## Problema

El dominio clínico necesita modelar tres conceptos temporales relacionados pero distintos:
1. Horario laboral oficial (contrato/compromiso)
2. Disponibilidad semanal planificada (agenda)
3. Slots operativos concretos (citas)

No está claro si estos conceptos son uno solo con diferentes nombres o si deben ser Value Objects separados.

## Decisión

Tres Value Objects distintos con semánticas diferentes:

### 1. WorkingHours (Contractual)
- **Representa:** Jornada laboral formal declarada por el profesional
- **Estabilidad:** Alta (definido por contrato)
- **Uso:** Validar cumplimiento ético, trazabilidad contractual

```java
public record WorkingHours(int declaredHoursPerWeek) {
    public boolean isCompliantWith(WeeklyAvailability availability) {
        return availability.totalHours() >= declaredHoursPerWeek;
    }
}
```

### 2. WeeklyAvailability (Planificación)
- **Representa:** Plan semanal de disponibilidad para agendar
- **Estabilidad:** Media (puede ajustarse semana a semana)
- **Uso:** Planificación de agenda, cálculo de capacidad

```java
public final class WeeklyAvailability {
    private final List<TimeSlot> slots;
    
    public int totalHours() {
        return slots.stream().mapToInt(TimeSlot::durationHours).sum();
    }
}
```

### 3. TimeSlot (Operacional)
- **Representa:** Bloque de tiempo concreto donde el profesional está disponible
- **Estabilidad:** Baja (cambia por vacaciones, emergencias)
- **Uso:** Validar agendamiento, detectar solapamientos, calcular duración

```java
public record TimeSlot(
    DayOfWeek dayOfWeek,
    LocalTime start,
    LocalTime end
) {
    public int durationHours() {
        return (int) Duration.between(start, end).toHours();
    }
    
    public boolean overlapsWith(TimeSlot other) {
        return dayOfWeek == other.dayOfWeek &&
               start.isBefore(other.end) && 
               end.isAfter(other.start);
    }
}
```

## Relación entre conceptos

```
WorkingHours (40h/semana) ← CONTRATO
    ↓ debe cumplirse con
WeeklyAvailability (42h planificadas) ← PLAN
    ↓ se compone de
List<TimeSlot> (slots concretos) ← EJECUCIÓN
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Un solo VO `WorkingTime` | Mezcla responsabilidades: contrato, plan y ejecución son conceptos distintos |
| TimeSlot con horas declaradas | TimeSlot es operativo, no debe conocer compromisos contractuales |
| WorkingHours con lista de slots | WorkingHours es declarativo (40h), no debe conocer cómo se distribuyen |

## Consecuencias

**Ganamos:**
- Separación semántica clara
- Trazabilidad: se puede auditar si la disponibilidad cumple lo declarado
- Flexibilidad: TimeSlots pueden cambiar sin afectar el contrato

**Perdemos:**
- Tres VOs en lugar de uno (mayor complejidad inicial)
- Requiere sincronización: cambios en TimeSlots pueden hacer que WeeklyAvailability no cumpla WorkingHours

## Validaciones cruzadas

```java
public class Dentist {
    private final WorkingHours workingHours;
    private final Schedule schedule;
    
    public Outcome<Void> validateScheduleCompliance() {
        if (!workingHours.isCompliantWith(schedule.getWeeklyAvailability())) {
            return Outcome.fail(
                ErrorCatalogXD.ERR_DENTIST_SCHEDULE_BELOW_WORKING_HOURS
            );
        }
        return Outcome.ok();
    }
}
```

## Métodos por VO

### WorkingHours
- `isCompliantWith(WeeklyAvailability)` → validación contractual
- `declaredHoursPerWeek()` → getter

### WeeklyAvailability
- `totalHours()` → métrica de disponibilidad
- `getSlots()` → acceso a slots
- `addSlot(TimeSlot)` → modificar plan

### TimeSlot
- `durationHours()` → cálculo operativo
- `overlapsWith(TimeSlot)` → detección de conflictos
- `contains(LocalDateTime)` → verificar si un momento cae en el slot

## Escenarios de uso

**Caso 1: Agendar cita**
```java
// Usar TimeSlot (operacional)
Optional<TimeSlot> availableSlot = schedule.findAvailableSlot(startTime);
if (availableSlot.isEmpty()) {
    throw new BusinessRuleViolationException("No hay slots disponibles");
}
```

**Caso 2: Auditar cumplimiento contractual**
```java
// Usar WorkingHours (contractual)
if (!dentist.getWorkingHours().isCompliantWith(schedule.getWeeklyAvailability())) {
    logger.warn("Dentist {} below contracted hours", dentistId);
}
```

**Caso 3: Calcular capacidad semanal**
```java
// Usar WeeklyAvailability (planificación)
int totalCapacity = schedule.getWeeklyAvailability().totalHours();
```