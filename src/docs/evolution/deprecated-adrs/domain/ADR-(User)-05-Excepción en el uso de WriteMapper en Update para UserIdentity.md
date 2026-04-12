

# ADR-05 (User): Excepción en el uso de WriteMapper en Update para UserIdentity

- **Fecha**: 2026-01-31
- **Estado:** Superado por [ADR-(User)-06-Estrategia dual de construcción en Value Objects (of - create) para simplificar mappers](../../../architecture/decisions/domain/authentication/ADR-%28User%29-06-Estrategia%20dual%20de%20construcci%C3%B3n%20en%20Value%20Objects%20%28of%20-%20create%29%20para%20simplificar%20mappers.md)
- **Categoría**: Dominio
- **Autor:** David Stiven Sanclemente

---

> **Nota histórica:** Este ADR documentaba una excepción puntual para `UserIdentity` que ya no es necesaria.  
> La solución definitiva es la estrategia dual `of()` / `create()` en todos los Value Objects, documentada en ADR-User-06.

## Problema

En la arquitectura se definió que los mappers de escritura (`WriteMapper`) deben encargarse de todas las conversiones de DTO → dominio, con el objetivo de mantener los adapters limpios y consistentes. Esta regla se aplica en otros adapters, tanto en operaciones de creación como de actualización.

Sin embargo, en el agregado **UserIdentity** surge una problemática: el método de dominio `editUserData(...)` ya encapsula la lógica de negocio y devuelve un `Outcome`. Si se fuerza el uso del `WriteMapper` en *update*, se aplican cambios directamente sobre el agregado sin pasar por el método de dominio, lo que obliga a duplicar validaciones en la capa de aplicación para poder lanzar el `Outcome`. Esto rompe la coherencia y genera redundancia.

---

## Decisión

No usar el `WriteMapper` en operaciones de *update* para el agregado **UserIdentity**.  
En su lugar, el Application Service invocará directamente los métodos de dominio (`editUserData`, `deactivate`, etc.), garantizando que las validaciones y el `Outcome` se gestionen dentro del dominio.

**Regla:**
- `WriteMapper` se usa en **create** para construir nuevos agregados.
- En **update**, se invocan directamente los métodos de dominio cuando estos devuelven un `Outcome`.
- Esta decisión es **una excepción específica para UserIdentity**. En los demás adapters se mantiene la convención de usar `WriteMapper` en create y update.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Usar `WriteMapper` también en update | Obliga a duplicar validaciones en la capa de aplicación, perdiendo el valor del `Outcome`. |
| Mantener ambos enfoques (mapper + dominio) | Genera inconsistencia y confusión sobre qué capa es responsable de validar reglas. |
| Eliminar `Outcome` y dejar validaciones en aplicación | Rompe el principio de encapsular reglas en el dominio y debilita el modelo. |

---

## Consecuencias

### Ganamos
- Uso consistente del `Outcome` en el dominio.
- Evitamos duplicar lógica de validación en la capa de aplicación.
- Claridad en la responsabilidad: el dominio valida, la aplicación orquesta.

### Perdemos
- Menor homogeneidad: el `WriteMapper` no se usa en update en este caso.
- Excepción puntual que debe ser documentada para evitar confusión futura.
- El Application Service construye directamente algunos Value Objects en update.

---

## Implementación

```java
@Override
public ReadUserIdentityDto editUserData(UpdateUserIdentityDto dto) {
    UserIdentity user = userIdentityRepository.findIdentity(UserId.from(dto.id()));

    Outcome<UserIdentity> outcome = user.editUserData(
        new UserName(dto.name()),
        new Email(dto.email()),
        HashedPassword.fromPlainText(dto.password()),
        Instant.now()
    );

    if (outcome.isFailure()) {
        throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
    }

    userIdentityRepository.save(user);
    return readMapper.toDto(user);
}
```

---

## Notas adicionales

- Esta decisión es **una excepción puntual para UserIdentity**.
- En otros adapters se mantiene la regla general: `WriteMapper` se usa tanto en create como en update.
- El método `dtoUpdateToUserIdentity(UpdateUserIdentityDto dto, UserIdentity user)` se elimina del `WriteMapper` porque no es útil en este caso.
- Documentar esta excepción asegura que futuros desarrolladores comprendan el motivo y no lo interpreten como un cambio de la regla general.

