# ADR-02: Implementación estratégica de Value Objects (VO) para encapsular lógica semántica y validar reglas clínicas

## Estado
Aprobado

## Contexto

Aunque algunos Value Objects comenzaron a implementarse antes de la migración arquitectónica, su uso era parcial y no siempre respetaba los principios semánticos que justifican su existencia. Con la migración hacia arquitectura hexagonal, se decidió formalizar el uso de VO como parte esencial del modelo clínico, permitiendo encapsular lógica sensible, validar reglas por operación y evitar degeneración semántica.

Esta decisión responde a la necesidad de construir un sistema clínico exhibible, trazable y ético, donde cada dato sensible esté representado por una estructura que pueda validar, justificar y evolucionar sin contaminar el dominio ni duplicar lógica.

## Decisión

Se adopta el uso sistemático de Value Objects (VO) para representar datos sensibles, encapsular lógica de validación y delegar responsabilidades semánticas fuera de los servicios y controladores.

Cada VO será:

- Inmutable.
- Validado en su construcción.
- Capaz de expresar reglas clínicas específicas.
- Utilizado en entidades, DTOs y flujos operativos.
- Documentado como parte de la evolución legítima del modelo.



## Justificación semántica

El uso de VO permite:

- Validar reglas clínicas en el momento de construcción del objeto.
- Evitar lógica duplicada en servicios, controladores o entidades.
- Delegar responsabilidades éticas a estructuras semánticas.
- Facilitar test unitarios y trazabilidad de errores.
- Separar semántica operativa de detalles técnicos.
- Construir un modelo exhibible y defendible ante auditores, clínicos y desarrolladores.

Ejemplo: el VO Dni validad formato y unicidad antes de ser aceptado por el sistema. El VO RangoHorario asegura que la hora de inicio sea menor que la hora de fin, evitando errores operativos. El VO EstadoTurno permite lanzar excepciones clínicas si se intenta modificar un turno cancelado.

## Impacto

- Refactorización de entidades para usar VO en lugar de tipos primitivos.
- Delegación de validaciones a VO, reduciendo acoplamiento en servicios.
- Creación de tests unitarios por VO.
- Mejora en la trazabilidad de errores clínicos.
- Base sólida para evolución semántica del modelo.

## Consecuencias

- Mayor coherencia entre reglas clínicas y estructuras de datos.
- Reducción de errores operativos por validaciones dispersas.
- Facilidad para extender el modelo con nuevos VO.
- Posibilidad de documentar reglas por operación en cada VO.
- Evolución legítima del sistema sin pérdida de contexto.

## Proyección

Se espera implementar nuevos VO en futuras etapas, incluyendo:

- Edad → Con método requiresGuardian() para validar necesidad de responsable.
- TipoSangre → Para representar compatibilidad clínica.
- DuracionTurno → Para generar slots dinámicos.
- CanalReserva → Para trazabilidad de origen del turno.
- MotivoCancelacion → Para justificar cancelaciones clínicas.

## VO implementados hasta la fecha

```text
Dni                → Valida formato y unicidad del documento.
NombreCompleto     → Encapsula nombre y apellido, con validación de longitud y caracteres.
Telefono           → Valida formato y evita duplicados.
Direccion          → Estructura semántica de ubicación (calle, ciudad, código postal, país).
Email              → Valida formato y unicidad del correo electrónico.
EstadoHorario      → Representa el estado de un horario clínico (activo, inactivo, bloqueado).
EstadoDisponibilidad → Indica si una disponibilidad está habilitada, bloqueada o agotada.
EstadoTurno        → Estado del turno (pendiente, confirmado, cancelado).
EstadoCita         → Estado clínico de la cita (programada, atendida, ausente).
RangoHorario       → Encapsula hora de inicio y fin, con validación de coherencia.
``` 
Cada nuevo VO será documentado como parte de la evolución semántica del sistema, y vinculado a su ADR correspondiente.
