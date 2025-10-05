# ADR-01: Migración a arquitectura hexagonal como inicio de trazabilidad semántica, ética y evolución legítima

## Estado
Aprobado

## Contexto

Este proyecto clínico nació el 13 de marzo de 2025 como una iniciativa personal para salir de un periodo de estancamiento profesional. Tras haber realizado varios cursos de Spring Boot, sentía que el aprendizaje teórico no era suficiente. Ese día decidí escribirle a un amigo que también estudia programación; él me conectó con alguien que ya trabaja en la industria, y le pedí consejos concretos para avanzar.

La respuesta fue inesperada: “Cualquier proyecto está bien, siempre que se evidencie buen uso de librerías, código limpio, arquitectura sólida, principios SOLID, patrones de diseño y buen modelo de datos.” Aunque en ese momento no me agradó, dos días después comprendí su valor al ver un video titulado *Creación de una API REST con Spring Boot 3, JPA, MySQL aplicando buenas prácticas*, del canal Dev Dominio. Ese video me reveló que los proyectos que había realizado anteriormente estaban mal construidos, sin estructura ni propósito real.

Motivado por esa revelación, decidí migrar y mejorar un proyecto anterior que había desarrollado en Java EE durante un curso del canal TODO CODE, dirigido por la ingeniera Lucina —una de mis principales mentoras. El proyecto original era una clínica odontológica con entidades como `Paciente`, `Odontólogo`, `Responsable`, `Turno`, `Login`, y reglas como:

- Validación de edad y responsable para menores.
- Asignación de turnos dentro del horario del odontólogo.
- Trazabilidad de accesos por rol.
- Informes agregados por día, obra social y profesional.

Aunque el curso no cubría el desarrollo completo, decidí tomarlo como base para aplicar todo lo aprendido y construir un sistema clínico real, trazable y ético.

## Evolución técnica antes de la migración

Durante la refactorización en Spring Boot, se implementaron mejoras significativas:

- Estructura modular con paquetes como `config`, `dto`, `entity`, `exception`, `mapper`, `metrics`, `repository`, `security`, `service`, `vo`, `web`, `util`.
- Uso de DTOs y mappers para separar capas.
- Manejo de excepciones personalizadas con `RestControllerAdvice` y catálogo de errores clínicos.
- Paginación en consultas.
- Validaciones con enums y anotaciones propias.
- Value Objects para encapsular lógica sensible (`Dni`, `NombreCompleto`, `Telefono`, etc.).
- Seguridad con Spring Security y control de permisos por rol.
- Programación funcional y test unitarios en repositorios.
- Validaciones éticas como edad mínima configurable para cada actor (`Responsable`, `Secretario`, `Odontólogo`).
- Restricción de duplicados en DNI y teléfono.

Se definieron servicios clínicos como `HorarioService`, `OdontologoService`, `PacienteService`, `ResponsableService`, `SecretarioService`, `TurnoService`, `UsuarioService`, con operaciones CRUD, búsquedas especializadas y trazabilidad.

## Diagrama de clases refactorizado

Se diseñó un diagrama de clases completo, incluyendo:

- Enums clínicos (`Afeccion`, `EstadoPago`, `TipoEventoClinico`, etc.).
- Value Objects (`Dni`, `Email`, `RangoHorario`, `EstadoTurno`, etc.).
- Superclase `Persona` mapeada para herencia semántica.
- Entidades clínicas (`Paciente`, `Odontólogo`, `Responsable`, `Secretario`).
- Seguridad (`Usuario`, `RolesEntity`, `PermissionEntity`).
- Agenda y turnos (`Horario`, `Disponibilidad`, `Turno`, `Cita`).
- Servicios y facturación (`Servicio`, `Facturacion`, `Pago`).
- Historia clínica (`HistorialClinico`, `EventoClinico`, `DocumentoClinico`).

Aunque algunas clases aún no se han implementado, el diagrama representa la visión estructural para la migración hacia arquitectura hexagonal.

## Decisión

Se migrará el sistema a una arquitectura hexagonal (Ports & Adapters), permitiendo:

- Separar semánticamente las capas de dominio, aplicación e infraestructura.
- Consolidar agregados clínicos como raíces semánticas que validan reglas por operación.
- Delegar validaciones a Value Objects y lanzar excepciones especializadas ante violaciones.
- Documentar cada decisión como ADR, vinculada al código y a los flujos clínicos.
- Facilitar la evolución legítima del modelo, con reversibilidad y exhibición internacional.
- Integrar nuevas funcionalidades (servicios, pagos, notificaciones, informes) sin comprometer el núcleo semántico.

## Justificación semántica

La arquitectura hexagonal permite que el dominio clínico sea autónomo, ético y coherente. Cada agregado puede expresar su lógica sin depender de detalles técnicos, y cada decisión puede ser documentada como parte de la narrativa del sistema.

Esto evita la degeneración semántica, permite validar reglas explícitamente, y facilita la reparación de flujos injustos o ambiguos. Además, permite incorporar nuevas funcionalidades como servicios externos o módulos operativos sin contaminar el núcleo clínico.

## Impacto

- Refactorización de agregados como `Appointment`, `Patient`, `Guardian`, `Dentist`, `Receptionist`.
- Consolidación de VO como `Age`, `WeeklyAvailability`, `TimeSlot`, `Dni`, `Email`, `RangoHorario`.
- Implementación de excepciones clínicas especializadas para reglas violadas.
- Creación de catálogos de errores clínicos con trazabilidad operativa.
- Redacción de ADRs por cada decisión semántica relevante.
- Diseño de puertos para servicios de pagos, notificaciones e informes.

## Consecuencias

- Mayor trazabilidad entre decisiones, código y flujos clínicos.
- Posibilidad de revisión ética y exhibición internacional.
- Separación clara entre semántica operativa y técnica.
- Evolución legítima del sistema sin pérdida de contexto.
- Declaración oficial del inicio de la documentación viva.