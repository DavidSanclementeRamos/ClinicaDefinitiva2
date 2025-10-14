# ADR: Sustitución de retorno booleano por excepciones semánticas en validación de agenda

## Contexto
El método `canScheduleBetween(start, end)` devolvía un `boolean` indicando si un intervalo podía ser agendado.  
```java
public boolean canScheduleBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || !start.isBefore(end)) return false;

        boolean coveredBySlot = weeklyAvailability.getSlots().stream()
                .anyMatch(slot -> slot.getDayOfWeek().equals(start.getDayOfWeek())
                        && slot.getDayOfWeek().equals(end.getDayOfWeek())
                        && !start.toLocalTime().isBefore(slot.getInicio())
                        && !end.toLocalTime().isAfter(slot.getFin()));

        boolean slotFree = appointments.stream()
                .filter(Appointment::isScheduled)
                .noneMatch(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), start, end));

        return coveredBySlot && slotFree;
    }
```
Este enfoque obligaba a la capa orquestadora (`Appointment`) a interpretar el resultado, mezclando responsabilidades y reduciendo la trazabilidad semántica de los errores.
```java
 // Validaciones de agenda
        if (!schedule.canScheduleBetween(newStart, newEnd)) {
            throw new BusinessRuleException("El horario no está disponible en la agenda.");
        }
```
## Decisión
Se reemplaza el retorno booleano por un método `validateScheduleBetween(start, end)` que lanza **excepciones semánticas** en caso de fallo:

- `InvalidScheduleException` → Fechas inválidas (nulos, orden incorrecto).
- `SlotNotCoveredException` → Intervalo fuera de la disponibilidad declarada.
- `SlotAlreadyTakenException` → Intervalo solapado con otra cita.
```java
public void validateScheduleBetween(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null || !start.isBefore(end)) {
        throw new InvalidScheduleException("Fechas inválidas: " + start + " - " + end);
    }

    boolean coveredBySlot = weeklyAvailability.getSlots().stream()
            .anyMatch(slot -> slot.getDayOfWeek().equals(start.getDayOfWeek())
                    && slot.getDayOfWeek().equals(end.getDayOfWeek())
                    && !start.toLocalTime().isBefore(slot.getInicio())
                    && !end.toLocalTime().isAfter(slot.getFin()));

    if (!coveredBySlot) {
        throw new SlotNotCoveredException(start, end);
    }

    boolean slotFree = appointments.stream()
            .filter(Appointment::isScheduled)
            .noneMatch(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), start, end));

    if (!slotFree) {
        throw new SlotAlreadyTakenException(start, end);
    }
}
```

De este modo:
- La validación es explícita y auditable.
- `Appointment` solo orquesta, sin lógica condicional.
- Cada error queda representado por un artefacto legítimo y exhibible.

## Consecuencias
### Positivas
- **Trazabilidad**: cada excepción tiene un nombre y mensaje claros, útiles para auditoría y comunicación con usuarios.
- **Separación de responsabilidades**: la validación vive en un servicio/validador; `Appointment` se limita a orquestar.
- **Extensibilidad**: nuevas reglas de validación pueden añadirse como nuevas excepciones sin romper la semántica existente.

### Negativas
- Se introduce complejidad en el manejo de excepciones (requiere `try/catch` en capas superiores).
- Posible sobrecarga si se abusa de excepciones para flujos esperados en lugar de errores.

## Estado
- Aceptado ✅
- Fecha: 2025-10-8


## Próximos pasos
- Documentar en la guía de desarrollo que **toda validación de agenda debe lanzar excepciones semánticas**.
- Ajustar pruebas unitarias para esperar excepciones en lugar de valores booleanos.
- Exhibir ejemplos de captura de excepciones en la capa de aplicación (UI/API).