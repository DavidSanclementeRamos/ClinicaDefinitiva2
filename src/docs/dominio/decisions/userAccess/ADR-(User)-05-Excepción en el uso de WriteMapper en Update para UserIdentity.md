Aquí tienes el ADR redactado siguiendo **exactamente la plantilla que me compartiste**, aplicado al caso de **no usar el `WriteMapper` en update para UserIdentity**:

---

# ADR-001: Excepción en el uso de WriteMapper en Update para UserIdentity

- **Fecha**: 2026-01-31
- **Estado**: Aprobado
- **Categoría**: Dominio

---

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

---

## Checklist antes de aprobar

- [x] El título es conciso y describe claramente la decisión
- [x] El problema está bien definido (≤3 párrafos)
- [x] La decisión es clara y tiene una regla explícita
- [x] Se documentan las alternativas descartadas
- [x] Se listan beneficios y costos reales
- [x] La implementación es mínima y clara
- [x] El estado está actualizado (Aprobado)

---

¿Quieres que redactemos ahora un **ADR-002** para documentar la decisión de inyectar `UserDeactivationPolicy` en el Application Service en lugar de pasarlo como parámetro en la interfaz?