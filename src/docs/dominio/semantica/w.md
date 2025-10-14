# WeeklyAvailability: HorasRegistradas

## Propósito
Validar que un actor clínico (ej. odontólogo) tenga al menos X horas registradas en su disponibilidad semanal.

## Semántica
- `HorasRegistradas(int mínimo)` devuelve `true` si el total de horas supera o iguala el mínimo.
- Se usa en validaciones éticas de creación de actores clínicos.

## Ejemplo
```java
weeklyAvailability.HorasRegistradas(10); // true si hay al menos 10 horas