# ADR-034: Implementación del Guardian de Reglas de Negocio

## Estado
Aceptado

## Contexto

Con la adopción de arquitectura hexagonal y la implementación sistemática de reglas de negocio por agregado (ver [ADR-032](ADR-032.md)), surgió la necesidad de *centralizar la validación ética y semántica del dominio*. Aunque cada entidad encapsula sus propias reglas, ciertas operaciones requieren validaciones cruzadas, contexto compartido y decisiones que no pertenecen a un solo Value Object.

Durante la etapa Spring Boot, las reglas eran robustas pero dispersas: se validaban en servicios, controladores o DTOs, lo que dificultaba la trazabilidad y la exhibición. Con la evolución hacia un modelo ético, se reconoció que el dominio necesitaba un *Guardian*: un componente que custodie las reglas, las invoque por operación y las documente como parte del lenguaje clínico.

## Decisión

Se implementa el *Guardian de Reglas de Negocio*, un componente del dominio que:

- *Invoca reglas por operación*, no por tipo de dato.
- *Valida condiciones clínicas, éticas y semánticas* antes de ejecutar acciones sensibles.
- *Centraliza la lógica de decisión* cuando involucra múltiples agregados o VO.
- *Registra el motivo, contexto y resultado* de cada validación.
- *Emite Outcomes clínicos* que pueden ser auditados, exhibidos o internacionalizados.

Este Guardian no reemplaza las validaciones locales (en VO o entidades), sino que las *coordina y protege* en operaciones de alto impacto.

## Ejemplos de uso

- Guardian.validarAgendamiento(Patient, Appointment) → verifica si el paciente está activo, no tiene bloqueos, y la cita no colisiona.
- Guardian.validarDesactivacion(Dentist) → verifica si el odontólogo tiene citas activas o tratamientos en curso.
- Guardian.validarCreacion(Patient) → verifica edad, contacto, documento y estado.
- Guardian.validarEdicion(Patient) → verifica si puede editarse, si tiene citas registradas, y si los cambios son sensibles.

## Consecuencias

- Mejora la trazabilidad ética del sistema.
- Evita duplicación de lógica en servicios y controladores.
- Permite auditar decisiones clínicas por operación.
- Facilita la evolución legítima del modelo.
- Convierte la validación en parte del lenguaje del dominio.

## Proyección

- Se integrará con el catálogo de errores clínicos (ver [ADR-033](ADR-033.md)).
- Se documentará cada operación validada como parte del modelo exhibible.
- Se emitirá un Outcome por cada validación, con motivo, contexto y resultado.
- Se habilitará la internacionalización de reglas y errores.

## Relación con otros ADR

- [ADR-032: Implementación sistemática de reglas de negocio por agregado](ADR-032.md)
- [ADR-033: Catálogo de errores clínicos por operación](ADR-033.md)
- [ADR-031: Implementación estratégica de Value Objects](ADR-031.md)