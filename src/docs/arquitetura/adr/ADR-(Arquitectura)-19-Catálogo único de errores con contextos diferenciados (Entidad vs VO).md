# ADR-19 (Arquitectura): Catálogo único de errores con contextos diferenciados (Entidad vs VO)

- **Estado:** Aceptado
- **Fecha:** 2025-12-22
- **Autor:** David

## Contexto
En el sistema Odontológico se manejan excepciones enriquecidas con información de:
- Catálogo de errores (`ErrorCatalog`)
- Contexto de la entidad (`EntityContext`)
- Código de entidad (`CodeEntity`)

Actualmente, los **Value Objects (VO)** también lanzan excepciones, pero no son agregados ni entidades. Esto genera un problema semántico: los VO deben usar `EntityCotext` (ej. `PERSON`), lo cual no refleja correctamente su naturaleza.  
La solución previa consistía en reutilizar `CodeEntity` mediante `valueOf()`, pero esto es poco óptimo y confunde la trazabilidad de errores.

## Decisión
Se define un **catálogo único de errores** (`ErrorCatalog`) para evitar duplicación, pero se introduce una **interfaz común `DomainContext`** con dos implementaciones:
- `EntityContext` → para entidades y agregados
- `VOContext` → para Value Objects

Las excepciones (`ClinicaDefinitivaException`, `ModelException`, `ValueObjectValidationException`) reciben un `DomainContext`, lo que permite distinguir claramente el origen del error sin romper la jerarquía existente.
## Diagrama
```
                ┌───────────────────────────────┐
                │       ClinicaDefinitivaException │
                │  + catalogo: ErrorCatalog        │
                │  + contexto: DomainContext     │
                │  + requestId: String             │
                └───────────────▲─────────────────┘
                                │
                                │
                ┌───────────────┴───────────────┐
                │         ModelException         │
                └───────────────▲───────────────┘
                                │
                                │
                ┌───────────────┴────────────────┐
                │ ValueObjectValidationException │
                └────────────────────────────────┘


┌───────────────────────┐        ┌───────────────────────┐
│   DomainContext     │◄──────►│   ErrorCatalog        │
│  <<interface>>         │        │  (enum único)         │
│  + getCode():String  │        │                      │
└───────────▲───────────┘        └───────────────────────┘
│
┌───────────┴───────────┐
│                       │
┌───────────────┐     ┌───────────────┐
│ EntityContext│     │ VOContext    │
│   (enum)       │     │   (enum)      │
└───────────────┘     └───────────────┘
```

## Consecuencias
### Positivas
- Se mantiene un catálogo único de errores, evitando duplicación.
- Se logra una separación semántica clara entre errores de VO y de Entidad.
- La jerarquía de excepciones se mantiene simple y extensible.
- La trazabilidad de errores mejora, ya que se puede identificar si el error proviene de un VO o de una Entidad.

### Negativas
- Se introduce un nuevo enum (`voContext`) y una interfaz (`DomainContext`), aumentando ligeramente la complejidad.
- Se requiere refactorizar las excepciones para aceptar `DomainContext` en lugar de `ContextoE`.

## Alternativas consideradas
- **Catálogo separado para VO y Entidad:** descartado por duplicación y mantenimiento más complejo.
- **Usar solo `EntityContext` para VO:** descartado por problemas semánticos y trazabilidad confusa.
- **Agregar un campo `scope` en `ErrorCatalog`:** viable, pero menos expresivo que separar contextos.

## Referencias
- [ADR-(Arquitectura)-18-Simplificación general de jerarquía de excepciones en el dominio.md](ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)