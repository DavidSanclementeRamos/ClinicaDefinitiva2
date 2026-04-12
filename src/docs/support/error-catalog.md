# Catálogo de errores

## Estado actual del catálogo

El sistema maneja errores mediante enums por agregado que implementan
`ErrorCatalog` (ver ADR-21 y ADR-52).

**El código fuente es la fuente de verdad.** Los enums están en:
`com.example.ClinicaDefinitiva.domain.*.error.*Error.java`

## Principios del diseño actual

- Cada error tiene código único (`ERR_*`), mensaje internacionalizable y contexto.
- Las excepciones lanzan `BusinessRuleViolationException` o
  `AggregateBusinessRuleViolationException`.
- La capa de aplicación traduce los errores a HTTP con `GlobalControllerAdvice`.

## Ejemplo de estructura actual

```java
public enum DentistError implements ErrorCatalog {
    ERR_DENTIST_AGE_INSUFFICIENT(
        "RN-DENTIST-001", 
        "error.dentist.age", 
        "El odontólogo debe tener al menos 25 años",
        EntityContext.DENTIST,
        VOContext.ACTOR
    );
}
```

## Material histórico

En una fase inicial del proyecto se documentó cada error individualmente
en archivos Markdown. Esta práctica se abandonó porque:

1. Con más de 250 errores activos, el mantenimiento era inviable.
2. Los archivos individuales divergían del código al actualizarse el dominio.
3. El código ya documenta los errores de forma más precisa que cualquier Markdown.

Esos archivos se conservan en
[`../evolution/deprecated-error-catalog/`](../evolution/deprecated-error-catalog/)
**exclusivamente como material pedagógico** que muestra el proceso de aprendizaje
del proyecto. **No representan el estado actual del sistema** — muchas entradas
están desactualizadas.

Para referencias cruzadas, consultar los ADRs: ADR-(Arquitectura)-18, [ADR-(Arquitectura)-19](../architecture/decisions/arch/ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md), [ADR-(Arquitectura)-21](../architecture/decisions/arch/ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md), ADR-(Arquitectura)-52, [ADR-(Arquitectura)-53](../architecture/decisions/arch/ADR-%28Arquitectura%29-53-Abandono%20del%20historial%20de%20cat%C3%A1logos%20eliminados%20y%20de%20la%20numeraci%C3%B3n%20inmutable.md).
