
# ADR-09 (Actores): Refactorización semántica con canScheduleAt(...)

- Estado: Superado por [ADR-(Cita)-09](../../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)
- Fecha: 2025-10-20
- Autor: David Stiven Sanclemente

## Contexto
El proceso de agendamiento clínico requería validar múltiples condiciones: estado del odontólogo y disponibilidad horaria.  
Estas validaciones estaban dispersas en el código cliente (Appointment), lo que dificultaba la trazabilidad y la evolución del modelo.

## Decisión
Se encapsuló la lógica de agendabilidad dentro de la entidad Dentist, mediante el método canScheduleAt(LocalDateTime dateTime), que combina las reglas de negocio relevantes.

Antes
```java
if (!dentist.isActive()) { ... }
if (!dentist.isAvailable(dateTime, day)) { ... }
```

Después
```java
if (!dentist.canScheduleAt(dateTime)) { ... }
```

## Justificación
- Mejora semántica: el modelo clínico expresa directamente la intención de agendamiento.
- Reducción de acoplamiento: Appointment no necesita conocer reglas internas de Dentist.
- Facilita testing y documentación: el método puede probarse y documentarse como unidad semántica.
- Principio de experto en información (DDD): Dentist concentra el conocimiento sobre su agendabilidad.
- Exhibición profesional: el modelo se presenta como una unidad coherente en auditorías y presentaciones.

## Consecuencias
- La lógica de negocio se centraliza en el dominio correcto.
- Se habilita trazabilidad semántica para rechazos de citas.
- Se mejora la expresividad del código y su documentación.

## Plan de implementación
1. Crear método canScheduleAt(LocalDateTime dateTime) en Dentist.
2. Internamente, validar estado (isActive()) y disponibilidad (isAvailable(dateTime)).
3. Refactorizar Appointment para usar únicamente canScheduleAt.
4. Integrar con catálogo de errores clínicos (ver ADR-03).
5. Añadir pruebas unitarias para escenarios de agendamiento válido e inválido.
6. Documentar reglas en docs/dominio/reglas-de-negocio/agendamiento.md.

## Ejemplo
```java
public boolean canScheduleAt(LocalDateTime dateTime) {
    return this.isActive() && this.isAvailable(dateTime);
}
```

## Relación con otros ADR
- [ADR-(Cita)-09-Ubicación de validaciones de estado y disponibilidad en el módulo de citas.md](../../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)