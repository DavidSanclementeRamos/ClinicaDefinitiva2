

---


# ADR: Revisión de Uso de Queries de Schedule en Validaciones de Reagendamiento

## Contexto
En el ADR "Validación de cambios de reagendamiento" se decidió que, para evitar duplicar lógica en Appointment, se podían reutilizar queries y cálculos de Schedule (ej. findAppointmentsWithinHours, findAppointmentsWithin) para validar políticas como tiempo mínimo de anticipación y ventana máxima.

```java
public List<Appointment> findAppointmentsWithinHours(int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(a -> TimeIntervalRules.overlaps(a.getStart(), a.getEnd(), now, limit))
                .toList();
    }
```
```java
 public List<Appointment> findAppointmentsWithin(int days) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime limit = now.plusDays(days);
    return appointments.stream()
            .filter(Appointment::isScheduled)
            .filter(a -> a.getStart().isAfter(now) && a.getStart().isBefore(limit))
            .toList();
}
```
La intención era mantener la lógica centralizada en Schedule y no repetir cálculos temporales en Appointment.

## Problema
Tras aplicar esta decisión, se identificó que dos validaciones específicas presentan ambigüedad semántica y dependencia innecesaria:

1. Tiempo mínimo de anticipación
   ```java
   if (schedule.findAppointmentsWithinHours(MINHOURSBEFORE_RESCHEDULE).contains(original)) {
       throw new BusinessRuleException("No se puede reagendar con menos de X horas de anticipación.");
   }
   ```
    - Ambigüedad: findAppointmentsWithinHours está pensada para listar citas futuras, no para validar una sola.
    - Dependencia innecesaria: se recorre la agenda completa para validar un único original.
    - Intención poco clara: la política es “la cita debe estar al menos X horas en el futuro”, lo cual se expresa mejor con:
      ```java
      if (original.getStart().isBefore(now.plusHours(MINHOURSBEFORE_RESCHEDULE))) { ... }
      ```

2. Ventana máxima
   ```java
   if (!schedule.findAppointmentsWithin(MAXMONTHSAHEAD * 30).contains(original)) {
       throw new BusinessRuleException("No se puede reagendar más allá de N meses en el futuro.");
   }
   ```
    - Ambigüedad: findAppointmentsWithin está diseñada para obtener listados, no para validar un límite absoluto.
    - Dependencia innecesaria: se usa una query de agenda para algo que puede resolverse directamente con:
      ```java
      if (newStart.isAfter(now.plusMonths(MAXMONTHSAHEAD))) { ... }
      ```

En ambos casos, lo que parecía una buena solución en el ADR anterior (reutilizar queries de Schedule) ahora genera ruido semántico: el código no expresa claramente la intención de negocio.

## Decisión
- Tiempo mínimo y ventana máxima se validarán directamente en Appointment usando comparaciones con LocalDateTime.now().
- Las queries de Schedule se reservan para casos donde la validación depende de la agenda completa (ej. conflictos, disponibilidad neta, capacidad diaria).

## Consecuencias
- Mayor claridad semántica: las políticas de tiempo se expresan directamente.
- Menor acoplamiento: Appointment no depende de queries de Schedule para validar sus invariantes.
- Uso correcto de queries: Schedule sigue siendo responsable de disponibilidad y conflictos, pero no de políticas absolutas de tiempo.
- Revisión del ADR previo: se ajusta la decisión original, reconociendo que la reutilización de queries no siempre es la mejor opción cuando compromete la semántica.

## Estado
✅ Adoptado.  
El ADR "Validación de cambios de reagendamiento" se actualiza para reflejar que:
- Tiempo mínimo y ventana máxima → validación directa en Appointment.
- Disponibilidad y conflictos → validación en Schedule.  
  `

---

