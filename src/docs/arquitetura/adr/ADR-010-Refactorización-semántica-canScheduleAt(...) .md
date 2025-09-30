
## ADR-010: Refactorización semántica: canScheduleAt(...)

## Contexto  
El proceso de agendamiento clínico requería validar múltiples condiciones: estado del odontólogo y disponibilidad horaria. Estas validaciones estaban dispersas en el código cliente (Appointment), lo que dificultaba la trazabilidad y la evolución del modelo.

## Decisión  
Se encapsuló la lógica de agendabilidad dentro de la entidad Dentist, mediante el método canScheduleAt(LocalDateTime dateTime), que combina las reglas de negocio relevantes.

Antes:

`java
if (!dentist.isActive()) { ... }
if (!dentist.isAvailable(dateTime, day)) { ... }
`

Después:

`java
if (!dentist.canScheduleAt(dateTime)) { ... }
`

## Justificación

- Mejora la semántica del modelo clínico.
- Reduce el acoplamiento entre Appointment y Dentist.
- Facilita testing, documentación y evolución.
- Refuerza el principio de experto en información (DDD).
- Permite exhibir el modelo como una unidad coherente en presentaciones internacionales.

## Consecuencias

- Se centraliza la lógica de negocio en el dominio correcto.
- Se habilita trazabilidad semántica para rechazos de citas.
- Se mejora la expresividad del código y su documentación