Perfecto 🙌, aquí tienes el **ADR-004** reescrito con la aclaración que pediste: deja claro que la problemática es **exclusiva de los agregados técnicos que usan `Outcome`**, porque en principio se esperaba que todo se manejara con `Outcome`, pero en el caso de *not found* la capa de aplicación lo maneja con excepción tradicional.

---

# ADR-004: Manejo de entidades no encontradas en Application Service para agregados técnicos con Outcome

- **Fecha**: 2026-01-31
- **Estado**: Aprobado
- **Categoría**: Arquitectura

---

## Problema

En ADRs anteriores se estableció que los **agregados técnicos** deben usar `Outcome` para reportar reglas de negocio y violaciones. Esto implica que, en principio, todo el flujo de interacción con estos agregados debería estar mediado por `Outcome`.

Sin embargo, surge una problemática **exclusiva de los agregados técnicos que usan `Outcome`**:
- Cuando el repositorio no encuentra la entidad (ej. `UserIdentity` inexistente), no se trata de una regla de negocio del agregado, sino de un problema técnico de persistencia.
- La duda nace porque, siguiendo la regla general, podría pensarse que `Outcome` debería extenderse para cubrir también el caso de "not found".
- Esto genera confusión: ¿se lanza excepción o se extiende `Outcome` para manejar ausencia técnica?

---

## Decisión

Para los **agregados técnicos que usan `Outcome`**, se decide que el caso de "entidad no encontrada" se maneja mediante **excepción en la capa de aplicación** (`AggregateNotFoundException`).

**Regla:**
- En todos los agregados técnicos, `Outcome` se usa para reportar reglas de negocio.
- El caso de “entidad no encontrada” no es una regla de negocio, sino un problema técnico de persistencia.
- Por lo tanto, incluso en agregados técnicos que usan `Outcome`, la capa de aplicación maneja “not found” con excepción (`AggregateNotFoundException`).
- Esto asegura que el mecanismo de `Outcome` no se extiende más allá de su propósito original: encapsular reglas de negocio dentro del agregado.

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Extender `Outcome` para incluir "not found" | Mezcla reglas de negocio con problemas técnicos, rompe la intención original de `Outcome`. |
| Devolver `null` silenciosamente | Oculta errores y genera comportamiento inesperado. |
| Usar `Optional` también en comandos | Complica el contrato de casos de uso; en comandos es más claro lanzar excepción. |

---

## Consecuencias

### Ganamos
- Claridad conceptual: `Outcome` sigue siendo exclusivo para reglas de negocio.
- Consistencia: el manejo de "not found" es uniforme en todos los agregados (técnicos y no técnicos).
- Separación de responsabilidades: dominio maneja reglas, aplicación maneja existencia.

### Perdemos
- Necesidad de definir y mantener excepciones específicas (`AggregateNotFoundException`).
- Los clientes de la API deben manejar excepciones en lugar de un flujo uniforme con `Outcome`.

---

## Implementación

Ejemplo en `UserApplicationService`:

```java
@Override
public ReadUserIdentityDto recordSuccessfulLogin(Long userId) {
    UserIdentity user = userRepository.findIdentity(UserId.from(userId));
    if (user == null) {
        throw new AggregateNotFoundException("UserIdentity not found with id " + userId);
    }

    Outcome<UserIdentity> outcome = user.recordSuccessfulLogin(Instant.now());
    if (outcome.isFailure()) {
        throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
    }

    userRepository.save(user);
    return readMapper.toDto(user);
}
```

---

## Notas adicionales

- Esta decisión aplica **solo al caso “not found” en agregados técnicos que usan `Outcome`**, como `UserIdentity`.
- En todos los demás escenarios de negocio, el uso de `Outcome` sigue siendo obligatorio en agregados técnicos.
- Documentar esta excepción evita confusión futura y asegura que no se interprete como un cambio de la regla general.
