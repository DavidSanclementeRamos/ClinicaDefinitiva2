# Proyecto Clínico Odontológico

Este proyecto nació el **13 de marzo de 2025** como una iniciativa personal para salir de un periodo de estancamiento profesional. Tras varios cursos de Spring Boot, comprendí que el aprendizaje teórico no era suficiente y decidí construir un sistema real que reflejara buenas prácticas arquitectónicas y semánticas.

## Motivación
Un consejo recibido fue clave: *“Cualquier proyecto está bien, siempre que se evidencie buen uso de librerías, código limpio, arquitectura sólida, principios SOLID, patrones de diseño y buen modelo de datos.”*  
Aunque inicialmente me incomodó, luego entendí su valor al ver ejemplos prácticos de APIs bien diseñadas. Esto me llevó a refactorizar un proyecto previo de clínica odontológica desarrollado en Java EE.

## Evolución técnica inicial
Durante la refactorización en Spring Boot se implementaron mejoras como:
- Estructura modular con paquetes (`config`, `dto`, `entity`, `exception`, `mapper`, `repository`, `security`, `service`, `vo`, `web`, `util`).
- Uso de DTOs y mappers para separar capas.
- Manejo de excepciones personalizadas con catálogo de errores clínicos.
- Paginación en consultas y validaciones con enums.
- Value Objects para encapsular lógica sensible (Dni, NombreCompleto, Teléfono).
- Seguridad con Spring Security y control de permisos por rol.
- Servicios clínicos como `HorarioService`, `OdontologoService`, `PacienteService`, etc.

## Visión estructural
Se diseñó un diagrama de clases con:
- Enums clínicos (Afección, EstadoPago, TipoEventoClinico).
- Value Objects (Dni, Email, RangoHorario).
- Superclase `Persona` para herencia semántica.
- Entidades clínicas (Paciente, Odontólogo, Responsable, Secretario).
- Seguridad (`Usuario`, `RolesEntity`, `PermissionEntity`).
- Agenda y turnos (`Horario`, `Disponibilidad`, `Turno`, `Cita`).
- Facturación y pagos (`Servicio`, `Facturacion`, `Pago`).
- Historia clínica (`HistorialClinico`, `EventoClinico`, `DocumentoClinico`).

Este README narra el **origen, motivación y evolución técnica** del proyecto, preparando el terreno para las decisiones arquitectónicas documentadas en los ADR.