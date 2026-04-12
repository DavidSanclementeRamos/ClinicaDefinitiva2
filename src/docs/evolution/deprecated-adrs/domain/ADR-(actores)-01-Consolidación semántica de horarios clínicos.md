# ADR-01-(Actores): Consolidación semántica de horarios clínicos

- **Estado:** Superado por [ADR-(Cita)-07](../../../architecture/decisions/domain/schedule/ADR-(Cita)-07-Consolidación%20de%20Shift%20como%20única%20fuente%20de%20verdad%20temporal.md) y [ADR-(Cita)-08](../../../architecture/decisions/domain/schedule/ADR-(Cita)-08-Transformación%20de%20Schedule%20en%20ScheduleQueryService.md)
- **Fecha:** 2025-09-25
- **Categoría:** Dominio 
- **Autor:** David Stiven Sanclemente

## Contexto
La entidad `Dentist` requiere validar que su disponibilidad semanal cumpla con un mínimo de horas (ej. 10 horas semanales).  
Actualmente, esta validación se realiza directamente en la entidad, accediendo a una lista de objetos `Availability`, cada uno con un `TimeSlot` de duración horaria.

Se detecta que existen dos clases (`TimeSlot` y `WorkingHours`) que modelan rangos horarios. Aunque tienen propósitos distintos, la lógica de validación semanal depende de un conjunto de disponibilidades, no de una sola unidad.

## Decisión
Se encapsula la lista de disponibilidades (`List<Availability>`) en un nuevo **Value Object** llamado `WeeklyAvailability`.  
Este VO representa la disponibilidad semanal completa del profesional y será responsable de aplicar reglas clínicas como:

- Validar si se cumple el mínimo de horas semanales.
- Calcular el total de horas disponibles.
- Evolucionar hacia reglas más complejas (mínimo de días, turnos, bloques válidos, etc.).

La lógica `tieneDisponibilidadMinima()` se delega a este VO, evitando acoplamiento en la entidad `Dentist` y preservando la semántica de `Availability` como unidad operativa.

## Justificación
- **Cohesión semántica:** `Availability` representa una unidad operativa; `WeeklyAvailability` representa el conjunto clínico.
- **Encapsulamiento ético:** Las reglas clínicas se agrupan en un VO trazable y evolutivo.
- **Evolución legítima:** Permite agregar nuevas reglas sin romper la semántica de las clases existentes.
- **Reutilización:** El VO puede ser usado por otros actores clínicos que requieran validación de disponibilidad.

## Consecuencias
- Se reduce el acoplamiento en la entidad `Dentist`.
- Se mejora la trazabilidad y validación ética del modelo.
- Se habilita la evolución semántica del sistema clínico.
- Se preserva la distinción entre horarios operativos (`TimeSlot`) y contractuales (`WorkingHours`).

## Plan de implementación
1. Crear clase `WeeklyAvailability` en el dominio (`com.clinica.domain.vo`).
2. Migrar lógica de validación desde `Dentist` hacia `WeeklyAvailability`.
3. Implementar métodos:
    - `hasMinimumHours(int minHours)`
    - `totalHours()`
    - `validateBlocks()` (para reglas futuras).
4. Refactorizar entidad `Dentist` para delegar validaciones al VO.
5. Añadir pruebas unitarias para `WeeklyAvailability`.
6. Documentar reglas en `docs/dominio/reglas-de-negocio/horarios.md`.

## Ejemplo
```java
WeeklyAvailability weeklyAvailability = new WeeklyAvailability(availabilities);
if (!weeklyAvailability.hasMinimumHours(10)) {
    throw new ClinicalValidationException(ERR_DENTIST_HORAS_INSUFICIENTES);
}
```
## Nota histórica:
 Este ADR documenta el diseño original de disponibilidad semanal mediante WeeklyAvailability. Dicho diseño fue reemplazado por la consolidación de Shift como única fuente de verdad temporal (ADR-Cita-07) y la transformación de Schedule en ScheduleQueryService (ADR-Cita-08). Se conserva como evidencia de la evolución del modelo.

