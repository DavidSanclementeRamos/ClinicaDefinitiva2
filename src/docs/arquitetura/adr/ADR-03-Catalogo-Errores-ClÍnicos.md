# ADR-003: Catálogo de errores clínicos por operación

## Estado
Aceptado

## Contexto

Durante la etapa Java EE del proyecto, los errores eran técnicos, genéricos y dispersos. Se usaban mensajes como "Bad Request", "NullPointerException" o "Validation failed" sin expresar la intención clínica ni la causa semántica del fallo. Esto generaba ambigüedad, frustración operativa y degeneración ética del sistema.

Con la migración a Spring Boot, se introdujo un *catálogo de errores robusto*, con excepciones personalizadas y manejo explícito de fallos mediante @RestControllerAdvice. Se diferenciaban errores de validación, autenticación, negocio y sistema, mejorando la experiencia de los desarrolladores que navegaban el código. Sin embargo, aunque el manejo técnico era sólido, *aún no se reconocía el valor semántico y clínico de los errores como parte del modelo de dominio*.

Fue recién con la adopción de arquitectura hexagonal que se reconoció la necesidad de *nombrar, justificar y documentar cada error clínico como parte del modelo ético*, vinculado a reglas de negocio por operación.

## Decisión

Se crea un *catálogo de errores clínicos por operación*, donde cada error:

- Tiene un *código único* (ERR_AGREGADO_CAUSA)
- Está *vinculado a una regla de negocio* documentada
- Expresa una *intención clínica, operativa o ética*
- Puede ser *exhibido, auditado y trazado* en el sistema

Este catálogo será:

- Referenciado desde cada regla por agregado
- Usado en excepciones del dominio (ClinicalValidationException)
- Documentado como parte del modelo internacionalizable
- Evolutivo: cada nuevo agregado o regla podrá extenderlo

## Ejemplos

- ERR_PATIENT_EDAD_INSUFICIENTE → paciente menor de edad sin responsable
- ERR_DENTIST_ESPECIALIDAD_INVALIDA → odontólogo con especialidad no reconocida
- ERR_SECRETARY_SECTOR_INVALIDO → secretario con sector no permitido
- ERR_APPOINTMENT_ESTADO_INVALIDO → cita con estado no reconocido

## Consecuencias

- Mejora la trazabilidad clínica del sistema
- Permite auditoría ética de fallos operativos
- Evita ambigüedad en flujos de validación
- Facilita la internacionalización del modelo
- Convierte los errores en parte del lenguaje del dominio

## Registro histórico

- En Java EE: errores genéricos y dispersos, sin semántica clínica.
- En Spring Boot: catálogo técnico robusto, con excepciones personalizadas y @RestControllerAdvice.
- En arquitectura hexagonal: reconocimiento del valor semántico y clínico de los errores, y su integración al modelo ético.

## Relación con otros ADR

- [ADR-032: Implementación sistemática de reglas de negocio por agregado](ADR-032.md)
- [ADR-031: Implementación estratégica de Value Objects](ADR-031.md)