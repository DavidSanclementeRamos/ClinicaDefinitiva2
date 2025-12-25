
# ADR-21 (Arquitectura): Catálogos de errores por agregado con interfaz común

- **Estado:** Propuesto
- **Fecha:** 2025-12-24
- **Autor:** David

## Contexto
Actualmente el sistema cuenta con un **catálogo único de errores** (`ErrorCatalog`) que concentra todas las validaciones de más de 30 agregados y varios Value Objects.  
Este enfoque, aunque consistente con la decisión tomada en [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md), genera problemas de **legibilidad y mantenibilidad**:

- El enum se vuelve demasiado extenso y difícil de navegar.
- Los errores de cada agregado no están agrupados semánticamente.
- La incorporación de nuevos agregados obliga a modificar un único archivo central, aumentando el riesgo de conflictos y errores.

Se busca una solución que mantenga la **consistencia de la interfaz de errores** pero permita **modularidad y claridad por agregado**.

## Decisión
Se define una **interfaz común `ErrorCatalog`** que expone los métodos:

```java
public interface ErrorCatalog {
    String getCode();
    String getKey();
    String getMessage();
}
```

Cada agregado tendrá su propio **enum de errores** que implementa esta interfaz. Ejemplo:

```java
public enum GuardianErrorCatalog implements ErrorCatalog {
    ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP(
        "RN-GUARDIAN-009",
        "error.guardian.cannotModifyRelationship",
        "No puede modificarse vínculo si ha autorizado tratamientos"
    ),
    ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON(
        "RN-GUARDIAN-010",
        "error.guardian.deactivationRequiresReason",
        "La desactivación requiere motivo obligatorio"
    );

    private final String code;
    private final String key;
    private final String message;

    GuardianErrorCatalog(String code, String key, String message) {
        this.code = code;
        this.key = key;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getKey() { return key; }
    public String getMessage() { return message; }
}
```

De esta forma, cada agregado mantiene su propio catálogo, pero todos comparten la misma interfaz, garantizando uniformidad.

## Diagrama
```
┌───────────────────────────────┐
│         ErrorCatalog          │
│        <<interface>>          │
│ + getCode(): String           │
│ + getKey(): String            │
│ + getMessage(): String        │
└───────────────▲───────────────┘
                │
 ┌──────────────┴───────────────┐
 │ GuardianErrorCatalog (enum)  │
 └──────────────┬───────────────┘
                │
 ┌──────────────┴───────────────┐
 │ ResponsibleErrorCatalog (enum)│
 └───────────────────────────────┘
```

## Consecuencias
### Positivas
- **Legibilidad:** los errores se agrupan por agregado, facilitando su comprensión.
- **Mantenibilidad:** agregar o modificar errores afecta solo al catálogo del agregado correspondiente.
- **Escalabilidad:** nuevos agregados pueden definir su propio catálogo sin tocar los existentes.
- **Consistencia:** la interfaz asegura un contrato uniforme para todos los catálogos.
- **Profesionalismo:** el diseño modular y claro mejora la calidad percibida del proyecto.

### Negativas
- **Fragmentación:** los errores ya no están en un único archivo, lo que puede dificultar búsquedas globales.
- **Unicidad de códigos:** se requiere disciplina para evitar duplicación de `code` entre catálogos.
- **Registro central opcional:** si se necesita un catálogo global (ej. para documentación o logging), habrá que implementar un `ErrorCatalogRegistry` que agregue todos los enums.

## Alternativas consideradas
- **Mantener catálogo único (ADR‑19):** más simple, pero poco legible y difícil de escalar.
- **Separar catálogos sin interfaz común:** descartado por falta de consistencia y riesgo de APIs divergentes.
- **Agregar un campo `scope` en `ErrorCatalog`:** viable, pero menos expresivo y no soluciona la legibilidad.

## Referencias
- [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md)


