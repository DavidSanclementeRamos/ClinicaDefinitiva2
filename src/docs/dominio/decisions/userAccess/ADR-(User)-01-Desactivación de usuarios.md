# ADR-01 (User): Desactivación de usuario en el agregado UserIdentity

- **Fecha**: 2025-11-12
- **Estado**: ❌ DEPRECADO - Reemplazado por ADR-023 y ADR-008
- **Categoría**: Dominio
- **Autor**: David

---

## ⚠️ NOTA HISTÓRICA

Este ADR se mantiene como registro de la evolución del diseño.

**Decisión inicial (incorrecta):** Colocar la lógica de desactivación directamente en cada agregado (Guardian, Dentist, etc.).

**Problema descubierto:** Esto rompía el encapsulamiento y generaba validaciones dispersas.

**Evolución:**
- ADR-023 (28/01/2026): Migró validación a `UserAccessValidator`
- ADR-008 (30/01/2026): Introdujo `UserDeactivationPolicy` como orquestador

Este documento se preserva como evidencia de la curva de aprendizaje en el manejo de operaciones que involucran múltiples agregados.

---

## Contexto Original

En el diseño inicial, algunos agregados como Guardian exponían métodos que permitían desactivar el usuario asociado (UserModel).

Esto rompía el principio de encapsulamiento y podía generar inconsistencias, ya que un agregado dependiente no debería mutar directamente el estado de otro agregado central.

UserModel (ahora UserIdentity) representa la identidad y el estado activo/inactivo del usuario. Por tanto, debería ser el único agregado con autoridad para modificar su propio estado.

## Decisión Original (Incorrecta)

La operación de desactivar un usuario sería movida al agregado UserModel.

Los agregados dependientes como Guardian solo expondrían métodos como `canDeactivateUser()` o `validateDeactivationAllowed()` para verificar restricciones.

## Por Qué Falló

1. **Validaciones cruzadas no resueltas:** No quedaba claro CÓMO UserModel validaría restricciones de Guardian, Dentist, etc.

2. **Violación de límites de agregado:** Para que UserModel validara, necesitaría conocer el estado de otros agregados, violando DDD.

3. **Falta de orquestación:** No se definió quién coordinaría las validaciones de múltiples agregados.

## Solución Correcta (Ver ADRs reemplazantes)

**ADR-023:** `UserAccessValidator` valida acceso del usuario (verificado, no bloqueado, activo)

**ADR-008:** `UserDeactivationPolicy` orquesta validaciones cruzadas (citas pendientes, tratamientos activos, etc.)

**Application Service:** Coordina ambos domain services antes de ejecutar `userIdentity.deactivate()`

## Lección Aprendida

Las operaciones que involucran múltiples agregados requieren:
1. Domain Services especializados para validaciones específicas
2. Policy como orquestador de múltiples validaciones
3. Application Service como coordinador final
4. El agregado solo ejecuta la acción si las validaciones pasan

No intentar poner toda la lógica en un solo agregado.

---

## Código Original (Propuesto pero nunca implementado)

```java
// En UserIdentity
public void deactivate() {
    if (!this.isActive()) {
        throw new UserAlreadyInactiveException(this.id);
    }
    this.state = UserState.INACTIVE;
}

// En Guardian
public boolean canDeactivateUser() {
    return this.patients.isEmpty();
}
```

## Código Correcto (Implementado en ADR-023 y ADR-008)

```java
// Application Service
public void deactivateUser(UserId userIdentityId) {
    // 1. Validar acceso (ADR-023)
    userAccessValidator.validateUserCanPerformSensitiveAction(userIdentityId, now);
    
    // 2. Validar restricciones de desactivación (ADR-008)
    Outcome validation = userDeactivationPolicy.validate(userIdentityId);
    if (validation.isFailure()) {
        throw new AggregateBusinessRuleViolationException(validation.getDetails());
    }
    
    // 3. Ejecutar acción en el agregado
    UserIdentity user = userIdentityRepository.findById(userIdentityId).orElseThrow();
    user.deactivate();
    userIdentityRepository.save(user);
}
```

---

## Relación con Otros ADRs

- **Reemplazado por:**
   - ADR-023: Validación de usuarios con UserAccessValidator
   - ADR-008: UserDeactivationPolicy como orquestador

- **Motivó:**
   - Investigación sobre validaciones cruzadas entre agregados
   - Diseño de domain services especializados