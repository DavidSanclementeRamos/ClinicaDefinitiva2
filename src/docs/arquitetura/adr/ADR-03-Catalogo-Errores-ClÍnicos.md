# ADR-003: Catálogo de errores clínicos por operación

## Estado
Aceptado

## Contexto

Durante las etapas iniciales del proyecto los errores se trataban como incidentes técnicos aislados. Mensajes como "Bad Request", "NullPointerException" o "Validation failed" dominaban los logs y las respuestas HTTP, lo que provocaba varias fallas operativas: pérdida de intención clínica en la notificación del fallo, dificultades para el diagnóstico por parte de clínicos y operadores, ambigüedad para auditorías y problemas para internacionalizar mensajes.
La migración a Spring Boot introdujo una capa técnica más ordenada: un catálogo de errores con códigos y excepciones personalizadas, y un manejo centralizado con @RestControllerAdvice. Esto redujo la dispersión técnica y mejoró la experiencia del equipo de desarrollo. Sin embargo, el catálogo siguió siendo un artefacto técnico alojado en capas de presentación o persistencia, sin estar plenamente integrado al dominio clínico: faltaba asociar cada error a la regla de negocio que lo causa, a su justificación ética y a su responsabilidad de agregado.
La adopción de la arquitectura hexagonal reveló la necesidad de tratar los errores no solo como señalizaciones técnicas, sino como elementos del lenguaje del dominio clínico: errores que deben nombrarse, justificarse, documentarse y versionarse junto con las reglas de negocio que los producen.

## Decisión

Adoptar un Catálogo de Errores Clínicos por Operación como artefacto del dominio; integrar dicho catálogo con las excepciones del dominio y con las capas de aplicación e infraestructura para su traducción a HTTP y logs.
Reglas concretas de la decisión:
• 	Cada error será una entrada en el catálogo con:
• 	código único formal (prefijo ERR_* por convención)
• 	mensaje base internacionalizable
• 	operación / regla de negocio que lo origina (referencia a ADR o documento de regla)
• 	tipo semántico (Validación clínica, Integridad, Autorización, Sistema)
• 	severidad sugerida (ERROR, WARN, INFO)
• 	contexto del agregado (p. ej. PACIENTE, ODONTOLOGO, TURNO)
• 	El catálogo se ubicará en el dominio (package: com.clinica.domain.error) y será la fuente de verdad.
• 	Las excepciones del dominio usarán el catálogo: crear excepción base de dominio ClinicaLValidationException (o mantener ClinicaDefinitivaException) que incluya referencia a la entrada del catálogo, contexto del agregado y requestId.
• 	La capa de aplicación lanzará esas excepciones cuando se violen reglas de negocio; la capa de infraestructura/web traducirá las excepciones a ErroResponse para el cliente y a niveles de log apropiados.
• 	El ErroResponse y el GlobalControllerAdvice se migrarán a infraestructura web (package: com.clinica.infrastructure.web).
• 	Cada nueva regla de negocio que introduzca posibles fallos deberá proponer su/ sus códigos de error en la plantilla de incorporación (incluyendo justificación clínica y mapeo a HTTP status).
• 	Mantener retrocompatibilidad: los códigos existentes del catálogo técnico se preservan; cuando un código cambia su semántica se publica una entrada de versión en el catálogo (changelog).


## Diseño y estructura propuesta
- Dominio
- com.clinica.domain.error.CatalogoError (enum o clase enriquecida)
- com.clinica.domain.error.CodigoEntidad (enum)
- com.clinica.domain.error.ContextoEntidad (enum)
- com.clinica.domain.exception.ClinicalValidationException (base) — contiene: CatalogoError, ContextoEntidad, detalle, requestId
- Aplicación
- com.clinica.application.exception.* (subexcepciones específicas si aplica)
- Casos de uso que lanzan las excepciones con la entrada de CatalogoError correspondiente
- Infraestructura / Web
- com.clinica.infrastructure.web.dto.ErrorResponse (DTO de salida de API)
- com.clinica.infrastructure.web.advice.GlobalControllerAdvice (traducción, logging y mapping a HttpStatus)
- Filtros/Interceptors (p. ej. RequestIdFilter) para garantizar requestId y contexto
- Documentación
- docs/errors/CatalogoErrores.md (lista exportable, changelog, mapeos a HTTP y gravedad)
- ADRs relacionados actualizados con referencias cruzadas

## Ejemplos (formato catálogo)
• 	ERR_PATIENT_EDAD_INSUFICIENTE — "Paciente menor de edad sin responsable" — Operación: CREAR_PACIENTE — Tipo: Validación clínica — Severidad: ERROR — HTTP: 400
• 	ERR_DENTIST_ESPECIALIDAD_INVALIDA — "Especialidad no reconocida" — Operación: ACTUALIZAR_ODONTOLOGO — Tipo: Validación clínica — Severidad: ERROR — HTTP: 400
• 	ERR_SECRETARY_SECTOR_INVALIDO — "Sector no permitido para secretario" — Operación: ASIGNAR_SECRETARIO — Tipo: Integridad de negocio — Severidad: ERROR — HTTP: 409
• 	ERR_APPOINTMENT_ESTADO_INVALIDO — "Estado de cita no reconocido" — Operación: CAMBIAR_ESTADO_CITA — Tipo: Validación de estado — Severidad: ERROR — HTTP: 400



## Consecuencias

• 	Positivas:
• 	Mejora neta de la trazabilidad clínica y ética.
• 	Facilita auditorías y análisis forense de decisiones operativas.
• 	El catálogo se convierte en artefacto versionado y auditable del dominio.
• 	Mejora la internacionalización al separar mensaje base (domain) y plantilla de presentación (infraestructura).
• 	Reduce ambigüedad en flujos de validación y mapeo a estados HTTP.
• 	Riesgos y mitigaciones:
• 	Riesgo de duplicación de errores entre equipos; mitigación: proceso de revisión para incorporación de nuevos códigos y changelog centralizado.
• 	Mayor carga inicial de documentación; mitigación: plantilla mínima obligatoria para cada nuevo error y revisión en el pipeline PR.
## Plan de migración (pasos prácticos)
- Crear com.clinica.domain.error y mover CatalogoError, CodigoEntidad, ContextoEntidad al dominio sin cambiar códigos ni mensajes.
- Implementar ClinicalValidationException en com.clinica.domain.exception (o refactorizar ClinicaDefinitivaException) con campos: CatalogoError, ContextoEntidad, detalle, requestId.
- Refactorizar casos de uso para lanzar las nuevas excepciones donde aplique; sustituir throws y mensajes libres por entradas del catálogo.
- Mover ErrorResponse y GlobalControllerAdvice a com.clinica.infrastructure.web, adaptar construcción de ErrorResponse para consumir CatalogoError y ContextoEntidad.
- Crear docs/errors/CatalogoErrores.md con plantilla para nuevas entradas (código, mensaje, operación, ADR referencia, tipo, severidad, HTTP sugerido).
- Añadir pruebas unitarias y de integración:
- Tests que verifican que cada excepción produce el ErrorResponse esperado.
- Tests de contrato API que validen códigos y mappings HTTP.
- Hacer release con changelog del catálogo y comunicar a equipos consumidores (API contract).
- Iterar: revisar en siguiente sprint TODOs y errores faltantes.
## Criterios de aceptación
- Todas las entradas de error referenciadas por reglas de negocio están en com.clinica.domain.error.
- Las excepciones del dominio contienen referencia a la entrada del catálogo y requestId.
- GlobalControllerAdvice traduce cualquier excepción de dominio en un ErrorResponse consistente con el catálogo.
- Existencia de documentación que liste código, mensaje, operación y justificación clínica.
- Pruebas que aseguren que el mapeo error → HTTP status → body cumple contrato.

## Pruebas y monitoreo
- Agregar pruebas unitarias para:
- Construcción de excepciones con CatalogoError.
- Traducción en GlobalControllerAdvice a ErrorResponse.
- Simular flujos end-to-end para casos clínicos críticos y validar logs (requestId, usuario, contexto).
- Instrumentar métricas: contar ocurrencias por código y contexto para detectar hotspots y cambios en reglas operativas.

## Registro histórico y trazabilidad
- Mantener en docs/errors/CHANGELOG.md cada adición o modificación de código con:
- fecha, autor, ADR o regla vinculada, motivo clínico y versión API.
- Registrar migraciones en el repositorio como commits con referencia a ADR-003.
## Relación con otros ADR
- ADR-032: Implementación sistemática de reglas de negocio por agregado
- ADR-031: Implementación estratégica de Value Objects
  Gobernanza y responsabilidad
- Propietario del catálogo: Equipo de Dominio (o rol de Product Owner clínico).
- Revisión y aprobación: Comité técnico + Revisor clínico para cada nuevo error.
- Proceso de incorporación: PR + plantilla de justificación clínica + actualización de CHANGELOG y tests.
## Rollback
- Si la migración introduce regresiones en consumidores, revertir la capa de infraestructura (GlobalControllerAdvice y ErrorResponse) a la versión anterior y publicar hotfix; mantener el catálogo en dominio en modo lectura hasta corregir incompatibilidades.





## Relación con otros ADR

- [ADR-032: Implementación sistemática de reglas de negocio por agregado](ADR-032.md)
- [ADR-031: Implementación estratégica de Value Objects](ADR-031.md)