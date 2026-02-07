


# ADR-05 (Dominio): Revisión de uso de queries de Schedule en validaciones de reagendamiento

- Estado: Adoptado
- Fecha: 2025-10-08
- Autor: David

## Contexto
En el ADR "Validación de cambios de reagendamiento" se decidió que, para evitar duplicar lógica en Appointment, se podían reutilizar queries y cálculos de Schedule (ej. findAppointmentsWithinHours, findAppointmentsWithin) para validar políticas como tiempo mínimo de anticipación y ventana máxima.

Tras aplicar esta decisión, se identificó que dos validaciones específicas presentan ambigüedad semántica y dependencia innecesaria:

Tiempo mínimo de anticipación
```java
if (schedule.findAppointmentsWithinHours(MINHOURSBEFORE_RESCHEDULE).contains(original)) {
    throw new BusinessRuleException("No se puede reagendar con menos de X horas de anticipación.");
}
```
 Problemas:
- findAppointmentsWithinHours está pensada para listar citas futuras, no para validar una sola.
- Dependencia innecesaria: se recorre la agenda completa para validar un único original.
- Intención poco clara: la política es “la cita debe estar al menos X horas en el futuro”, lo cual se expresa mejor con:
  ```java
  if (original.getStart().isBefore(now.plusHours(MINHOURSBEFORE_RESCHEDULE))) { ... }
  ```

Ventana máxima
```java
if (!schedule.findAppointmentsWithin(MAXMONTHSAHEAD * 30).contains(original)) {
    throw new BusinessRuleException("No se puede reagendar más allá de N meses en el futuro.");
}
```
Problemas:
- findAppointmentsWithin está diseñada para obtener listados, no para validar un límite absoluto.
- Dependencia innecesaria: se usa una query de agenda para algo que puede resolverse directamente con:
  ```java
  if (newStart.isAfter(now.plusMonths(MAXMONTHSAHEAD))) { ... }
  ```

## Decisión
- Tiempo mínimo y ventana máxima → validación directa en Appointment usando comparaciones con LocalDateTime.now().
- Queries de Schedule → reservadas para casos donde la validación depende de la agenda completa (conflictos, disponibilidad neta, capacidad diaria).

Justificación
- Mayor claridad semántica: las políticas de tiempo se expresan directamente.
- Menor acoplamiento: Appointment no depende de queries de Schedule para validar sus invariantes.
- Uso correcto de queries: Schedule sigue siendo responsable de disponibilidad y conflictos, pero no de políticas absolutas de tiempo.
- Revisión del ADR previo: se ajusta la decisión original, reconociendo que la reutilización de queries no siempre es la mejor opción cuando compromete la semántica.

## Consecuencias
- Código más claro y semánticamente fuerte.
- Menor dependencia entre agregados.
- Queries de Schedule se usan solo en su contexto legítimo.
- Se refuerza la trazabilidad ética del modelo.

## Plan de implementación
1. Refactorizar Appointment para validar tiempo mínimo y ventana máxima directamente con LocalDateTime.now().
2. Eliminar dependencias innecesarias de Schedule en estas validaciones.
3. Mantener queries de Schedule para conflictos y disponibilidad.
4. Actualizar pruebas unitarias para reflejar la nueva lógica.
5. Documentar ajuste en docs/dominio/reglas-de-negocio/reagendamiento.md.

## Ejemplo
```java
// Validación directa en Appointment
if (original.getStart().isBefore(LocalDateTime.now().plusHours(MINHOURSBEFORE_RESCHEDULE))) {
throw new MinimumAnticipationException(original.getStart());
}

if (newStart.isAfter(LocalDateTime.now().plusMonths(MAXMONTHSAHEAD))) {
throw new MaximumWindowExceededException(newStart);
}
```

## Relación con otros ADR
- ADR-17 (Dominio): Validaciones de Reagendamiento de Citas.
- ADR-18 (Dominio): Sustitución de retorno booleano por excepciones semánticas en validación de agenda.
- ADR-13 (Dominio): Mantener y refinar upcomingWithinHours como consulta de soporte.  
  