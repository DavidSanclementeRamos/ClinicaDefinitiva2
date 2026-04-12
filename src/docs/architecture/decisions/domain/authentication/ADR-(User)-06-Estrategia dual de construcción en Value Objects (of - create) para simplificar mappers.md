
```markdown
# ADR-User-06: Estrategia dual de construcción en Value Objects (of / create) para simplificar mappers

- **Fecha**: 2026-04-08
- **Estado**: Aprobado
- **Categoría**: Dominio / Value Objects
- **Supera**: [ADR-(User)-05-Excepción en el uso de WriteMapper en Update para UserIdentity.md](../../../../evolution/deprecated-adrs/domain/ADR-%28User%29-05-Excepci%C3%B3n%20en%20el%20uso%20de%20WriteMapper%20en%20Update%20para%20UserIdentity.md)
- **Autor:** David Stiven Sanclemente

---

## Problema

Inicialmente, los Value Objects del dominio (ej. `Email`, `HashedPassword`, `UserIdentityName`) exponían únicamente un método de fábrica que retornaba `Outcome<VO>`. Esto era adecuado para contextos donde se requiere acumular errores (como en políticas de validación), pero forzaba a los mappers de escritura a lidiar con `Outcome`, lo que complicaba el código y llevó a crear excepciones puntuales como la documentada en ADR-005 (no usar `WriteMapper` en updates de `UserIdentity`).

Esta solución local no escalaba y generaba inconsistencias: unos módulos usaban `Outcome` en mappers, otros no.

---

## Decisión

**Cada Value Object que requiera validación expondrá dos métodos de fábrica:**

1. **`of(String valor)`** → Lanza `ValueObjectValidationException` si la validación falla.  
   - Usado en mappers (escritura y lectura), en Application Services, y en cualquier lugar donde se requiera fail-fast.
   - Simplifica el código de las capas superiores, ya que no tienen que manejar `Outcome`.

2. **`create(String valor)`** (o nombre semántico como `fromHash`) → Retorna `Outcome<VO>`.  
   - Usado en Domain Services, Policies y otros contextos donde se necesite acumular errores (ej. validaciones cruzadas).

**Regla:** Ningún Value Object debe exponer un constructor público. Siempre se accede a través de estos métodos de fábrica.

---

## Implementación en los VOs existentes

### Ejemplo: `Email`

```java
public final class Email implements Serializable {
    private final String value;

    private Email(String normalized) { ... }  // constructor privado

    // Lanza excepción (fail-fast)
    public static Email ofOrThrow(String email) { ... }

    // Retorna Outcome (para acumulación)
    public static Outcome<Email> of(String raw) { ... }
}
```

### Ejemplo: `HashedPassword`

```java
public final class HashedPassword {
    // Lanza excepción
    public static HashedPassword of(String rawHash) { ... }

    // Retorna Outcome
    public static Outcome<HashedPassword> fromHash(String rawHash) { ... }
}
```

### Ejemplo: `UserIdentityName`

```java
public final class UserIdentityName {
    // Lanza excepción
    public static UserIdentityName of(String value) { ... }

    // Retorna Outcome
    public static Outcome<UserIdentityName> create(String raw) { ... }
}
```

---

## Consecuencias

### Lo que ganamos

- **Consistencia en toda la base de código**: todos los VOs siguen el mismo patrón.
- **Mappers simples**: pueden usar `of()` sin manejar `Outcome`, eliminando la necesidad de excepciones como la de ADR-005.
- **Flexibilidad**: donde se necesite acumular errores, se usa `create()`.
- **Claridad semántica**: el nombre del método indica el comportamiento (excepción vs. outcome).

### Lo que perdemos

- **Dos métodos por VO** → aumenta ligeramente el número de métodos, pero el beneficio supera el costo.
- **Pequeña duplicación de lógica de validación** (ambos métodos llaman al mismo constructor privado, pero la validación se hace en el constructor, por lo que no hay duplicación real).

### Impacto en ADRs anteriores

- **ADR-005 (User)** queda **superado**. La excepción que documentaba ya no es necesaria, pues ahora los mappers pueden usar `of()` en updates sin problemas.
- Se actualizará el estado de ADR-005 a `Superado por ADR-User-06`.

---

## Relación con otros ADRs

- **Complementa** cualquier ADR que defina Value Objects.
- **Resuelve** la inconsistencia planteada en ADR-005.

```

