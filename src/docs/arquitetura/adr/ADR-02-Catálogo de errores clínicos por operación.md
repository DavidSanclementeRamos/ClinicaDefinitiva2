

# ADR-02: Catálogo de errores clínicos por operación

- **Estado:** Aceptado
- **Fecha:** 2025-09-15
- **Autor:** David

## Contexto
En las etapas iniciales del proyecto los errores se trataban como incidentes técnicos aislados, con mensajes genéricos como *Bad Request* o *NullPointerException*. Esto provocaba fallas operativas: pérdida de intención clínica en la notificación, dificultades para diagnóstico, ambigüedad en auditorías y problemas de internacionalización.

La migración a Spring Boot introdujo un catálogo técnico de errores con excepciones personalizadas y manejo centralizado vía `@RestControllerAdvice`. Aunque mejoró la experiencia del equipo, el catálogo seguía siendo un artefacto técnico, sin integración plena al dominio clínico.

La adopción de arquitectura hexagonal reveló la necesidad de tratar los errores como parte del **lenguaje del dominio clínico**: nombrados, justificados, documentados y versionados junto con las reglas de negocio que los producen.

## Decisión
Adoptar un **Catálogo de Errores Clínicos por Operación** como artefacto del dominio, integrado con excepciones y capas de aplicación e infraestructura.

### Reglas de la decisión
- Cada error será una entrada con:
  - Código único formal (`ERR_*`)
  - Mensaje base internacionalizable
  - Operación / regla de negocio que lo origina (referencia a ADR o documento de regla)
  - Tipo semántico (Validación clínica, Integridad, Autorización, Sistema)
  - Severidad sugerida (ERROR, WARN, INFO)
  - Contexto del agregado (Paciente, Odontólogo, Turno, etc.)
- El catálogo se ubicará en `com.clinica.domain.error` y será la fuente de verdad.
- Excepciones del dominio usarán el catálogo (`ClinicalValidationException`) con referencia a la entrada, contexto y `requestId`.
- La capa de aplicación lanzará estas excepciones; la infraestructura/web las traducirá a `ErrorResponse` y logs.
- Cada nueva regla de negocio deberá proponer sus códigos de error en la plantilla de incorporación.
- Retrocompatibilidad: los códigos existentes se preservan; cambios de semántica se registran en un changelog versionado.

## Diseño y estructura propuesta
- **Dominio**
  - `com.clinica.domain.error.CatalogoError`
  - `com.clinica.domain.error.CodigoEntidad`
  - `com.clinica.domain.error.ContextoEntidad`
  - `com.clinica.domain.exception.ClinicalValidationException`
- **Aplicación**
  - `com.clinica.application.exception.*`
  - Casos de uso lanzan excepciones con entradas del catálogo
- **Infraestructura / Web**
  - `ErrorResponse` (DTO)
  - `GlobalControllerAdvice` (traducción y logging)
  - Filtros/Interceptors para `requestId`
- **Documentación**
  - `docs/errors/CatalogoErrores.md` (lista exportable, changelog, mapeos a HTTP y severidad)

## Ejemplos
- `ERR_PATIENT_EDAD_INSUFICIENTE` — "Paciente menor de edad sin responsable" — Operación: CREAR_PACIENTE — Tipo: Validación clínica — Severidad: ERROR — HTTP: 400
- `ERR_DENTIST_ESPECIALIDAD_INVALIDA` — "Especialidad no reconocida" — Operación: ACTUALIZAR_ODONTOLOGO — Tipo: Validación clínica — Severidad: ERROR — HTTP: 400
- `ERR_SECRETARY_SECTOR_INVALIDO` — "Sector no permitido para secretario" — Operación: ASIGNAR_SECRETARIO — Tipo: Integridad de negocio — Severidad: ERROR — HTTP: 409
- `ERR_APPOINTMENT_ESTADO_INVALIDO` — "Estado de cita no reconocido" — Operación: CAMBIAR_ESTADO_CITA — Tipo: Validación de estado — Severidad: ERROR — HTTP: 400

## Consecuencias
- **Positivas**
  - Mejora la trazabilidad clínica y ética.
  - Facilita auditorías y análisis forense.
  - Artefacto versionado y auditable del dominio.
  - Mejora internacionalización y reduce ambigüedad.
- **Riesgos y mitigaciones**
  - Riesgo de duplicación de errores → mitigación: proceso de revisión y changelog centralizado.
  - Mayor carga inicial de documentación → mitigación: plantilla mínima obligatoria y revisión en PR.

## Plan de migración
1. Crear `com.clinica.domain.error` y mover catálogo actual.
2. Implementar `ClinicalValidationException`.
3. Refactorizar casos de uso para lanzar excepciones del catálogo.
4. Mover `ErrorResponse` y `GlobalControllerAdvice` a infraestructura web.
5. Crear `docs/errors/CatalogoErrores.md` con plantilla para nuevas entradas.
6. Añadir pruebas unitarias e integración.
7. Publicar release con changelog y comunicar a equipos consumidores.

## Criterios de aceptación
- Todas las entradas de error están en `com.clinica.domain.error`.
- Excepciones del dominio contienen referencia al catálogo y `requestId`.
- `GlobalControllerAdvice` traduce excepciones en `ErrorResponse` consistente.
- Documentación lista código, mensaje, operación y justificación clínica.
- Pruebas aseguran mapeo error → HTTP status → body.

## Pruebas y monitoreo
- Unit tests para construcción de excepciones.
- Tests de traducción en `GlobalControllerAdvice`.
- Flujos end-to-end para casos clínicos críticos.
- Métricas: ocurrencias por código y contexto.


## Relación con otros ADR
- [ADR-01 Migración progresiva a arquitectura hexagonal](ADR-01-Migración%20progresiva%20a%20arquitectura%20hexagonal.md)


