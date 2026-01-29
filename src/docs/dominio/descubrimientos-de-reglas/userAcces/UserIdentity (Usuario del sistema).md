# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: UserIdentity (Usuario del sistema)

## Propósito
Representar la identidad de un usuario dentro del sistema de acceso. Este agregado gestiona autenticación, verificación, bloqueo por intentos fallidos y estado operativo. Asegura que solo usuarios activos y verificados puedan realizar acciones sensibles, protege contra ataques de fuerza bruta y garantiza trazabilidad de accesos.

**Estrategia de manejo de errores:** Este módulo utiliza exclusivamente **Outcome** para el manejo de errores, ya que la autenticación y autorización son flujos de control esperados donde los "fallos" no representan excepciones sino estados válidos del sistema.

---

## CREACIÓN
- El usuario debe tener un email válido y único en el sistema.
- La contraseña debe ser almacenada como `HashedPassword`, nunca en texto plano.
- El estado inicial por defecto es `ACTIVE`.
- El usuario se crea como **no verificado** (`verified = false`).
- Debe registrar nombre válido (`UserName`) con validaciones:
    - Mínimo 3 caracteres
    - Máximo 15 caracteres
    - No puede ser null o vacío
    - Se trimea automáticamente
- Debe registrar fecha de creación (`createdAt`).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si pasa la validación `canPerformSensitiveAction`:
    - Debe estar verificado
    - No debe estar bloqueado
    - Debe estar en estado `ACTIVE`
- Puede actualizar nombre, email y contraseña (siempre como `HashedPassword`).
- El email nuevo debe ser único y válido.
- Cambios sensibles deben registrar fecha y responsable.

---

## DESACTIVACIÓN / ELIMINACIÓN
- La eliminación física está prohibida; se maneja como cambio de estado (`UserStatus`).
- La desactivación requiere:
    - Pasar validación `canPerformSensitiveAction`
    - Cumplir política de desactivación (`UserDeactivationPolicy`)
- No puede desactivarse si está en medio de una sesión activa (infra debe validar).
- Estados válidos: `ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING_VERIFICATION`.
- Transiciones válidas definidas en `UserStatus.canTransitionTo()`:
    - `PENDING_VERIFICATION` → `ACTIVE` (después de verificar)
    - `ACTIVE` → `INACTIVE` (desactivación voluntaria)
    - `ACTIVE` → `SUSPENDED` (suspensión administrativa)
    - `INACTIVE` → `ACTIVE` (reactivación)
    - `SUSPENDED` → `ACTIVE` (levantamiento de suspensión)
- Transiciones inválidas:
    - `PENDING_VERIFICATION` → `SUSPENDED`
    - `INACTIVE` → `SUSPENDED`
    - Cualquier transición al mismo estado

---

## OPERACIONES DE DOMINIO

### Autenticación
- `recordFailedLogin(now, maxAttempts, lockDuration)` → Incrementa intentos fallidos y bloquea si supera el máximo.
    - Falla con `ERR_USER_ACCOUNT_LOCKED` si ya está bloqueado
    - Falla con `ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS` si alcanza el máximo
    - Falla con `ERR_USER_INVALID_CREDENTIALS` si no alcanza el máximo
- `recordSuccessfulLogin(now)` → Reinicia contador de intentos y actualiza `lastLoginAt`.
    - Falla con `ERR_USER_FAILED_ATTEMPTS_NOT_RESET` si está bloqueado
- `isLocked(now)` → Verifica si la cuenta está bloqueada hasta `lockedUntil`.

### Verificación
- `verify()` → Marca al usuario como verificado, solo una vez.
    - Falla con `ERR_USER_NOT_VERIFIED` si ya está verificado

### Validación de Elegibilidad
- `canPerformSensitiveAction(now)` → Verifica si el usuario está activo, verificado y no bloqueado.
    - Falla con `ERR_USER_NOT_VERIFIED` si no verificado
    - Falla con `ERR_USER_ACCOUNT_LOCKED` si bloqueado
    - Falla con `ERR_USER_INACTIVE` si no está en estado ACTIVE

### Gestión de Estado
- `editUserData(newName, newEmail, newPassword, now)` → Actualiza datos sensibles bajo reglas de negocio.
    - Requiere pasar `canPerformSensitiveAction`
- `deactivate(policy, now)` → Desactiva usuario según política.
    - Requiere pasar `canPerformSensitiveAction`
    - Falla con `ERR_USER_DEACTIVATION_CONSTRAINTS` si la política no permite
- `suspend(reason, now)` → Suspende temporalmente el usuario.
    - Falla con `ERR_USER_ALREADY_SUSPENDED` si ya está suspendido
    - Falla con `ERR_USER_SUSPENSION_REQUIRES_REASON` si no hay razón
- `reactivate(now)` → Reactiva usuario previamente suspendido o inactivo.
    - Falla con `ERR_USER_ALREADY_ACTIVE` si ya está activo
    - Falla con `ERR_USER_NOT_VERIFIED` si no está verificado
    - Limpia bloqueos y contador de intentos al reactivar

---

## INVARIANTES GLOBALES
- Un usuario debe tener email único (validado en repositorio).
- Un usuario activo debe estar verificado para realizar acciones sensibles.
- Un usuario bloqueado no puede iniciar sesión.
- La contraseña siempre debe ser un `HashedPassword`.
- El contador de intentos fallidos se reinicia solo en login exitoso.
- El bloqueo es temporal y se verifica contra `lockedUntil`.
- Solo se puede verificar una vez.
- Las transiciones de estado deben seguir el flujo definido en `UserStatus`.

---

## VALIDACIONES DE VALUE OBJECTS

### Email (VO)
**Reglas de validación:**
- No puede ser null → `ERR_EMAIL_NULL`
- No puede estar vacío después de trim → `ERR_EMAIL_EMPTY`
- Debe contener '@' con parte local y dominio → `ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN`
- Longitud total máxima: 254 caracteres → `ERR_EMAIL_LENGTH_EXCEEDED`
- Parte local máxima: 64 caracteres → `ERR_EMAIL_LOCAL_LENGTH_EXCEEDED`
- Parte dominio máxima: 253 caracteres → `ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED`
- Debe cumplir patrón regex válido → `ERR_EMAIL_INVALID_FORMAT`
- Dominio no puede empezar o terminar con guión → `ERR_EMAIL_DOMAIN_INVALID_DASH`
- Dominio no puede tener puntos consecutivos → `ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS`

**Normalización:**
- Se aplica trim
- El dominio se convierte a lowercase
- Formato final: `local@domain`

### HashedPassword (VO)
**Reglas de validación:**
- No puede ser null → `ERR_USER_PASSWORD_HASH_NULL`
- No puede estar vacío → `ERR_USER_PASSWORD_HASH_EMPTY`
- Solo acepta valores ya hasheados (el hashing es responsabilidad de infraestructura)

### UserName (VO)
**Reglas de validación:**
- No puede ser null → `ERR_USER_NAME_NULL`
- No puede estar vacío después de trim → `ERR_USER_NAME_EMPTY`
- Mínimo 3 caracteres → `ERR_USER_NAME_TOO_SHORT`
- Máximo 15 caracteres → `ERR_USER_NAME_TOO_LONG`

**Normalización:**
- Se aplica trim automáticamente

### UserStatus (VO)
**Estados válidos:**
- `ACTIVE`: Usuario operativo
- `INACTIVE`: Usuario desactivado
- `SUSPENDED`: Usuario suspendido temporalmente
- `PENDING_VERIFICATION`: Usuario pendiente de verificación

**Validación de transiciones:**
- Método `canTransitionTo(State newState)` valida si una transición es permitida
- No permite transiciones al mismo estado
- Implementa matriz de transiciones válidas

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada intento fallido con incremento de contador.
- Se registra fecha de último login (`lastLoginAt`) en login exitoso.
- Se registra fecha de bloqueo (`lockedUntil`) cuando se alcanza el máximo de intentos.
- Se registra motivo de suspensión (parámetro obligatorio).
- Se debe emitir eventos de dominio (responsabilidad de capa de aplicación):
    - `UserLockedEvent`
    - `UserVerifiedEvent`
    - `UserProfileUpdated`
    - `UserRegistered`
    - `UserLoggedIn`
    - `UserDeactivated`

---

## CATÁLOGO DE ERRORES

### Errores del Agregado UserIdentity
- `ERR_USER_ACCOUNT_LOCKED`: Cuenta bloqueada temporalmente
- `ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS`: Cuenta bloqueada por intentos fallidos
- `ERR_USER_INVALID_CREDENTIALS`: Credenciales inválidas
- `ERR_USER_FAILED_ATTEMPTS_NOT_RESET`: No se puede resetear intentos (cuenta bloqueada)
- `ERR_USER_NOT_VERIFIED`: Usuario no verificado
- `ERR_USER_DEACTIVATION_CONSTRAINTS`: No se cumplen restricciones para desactivación
- `ERR_USER_ALREADY_SUSPENDED`: Usuario ya está suspendido
- `ERR_USER_SUSPENSION_REQUIRES_REASON`: Se requiere razón para suspensión
- `ERR_USER_ALREADY_ACTIVE`: Usuario ya está activo

### Errores de Value Objects
- `ERR_EMAIL_NULL`: Email es null
- `ERR_EMAIL_EMPTY`: Email está vacío
- `ERR_EMAIL_MISSING_LOCAL_OR_DOMAIN`: Email sin parte local o dominio
- `ERR_EMAIL_LENGTH_EXCEEDED`: Email excede 254 caracteres
- `ERR_EMAIL_LOCAL_LENGTH_EXCEEDED`: Parte local excede 64 caracteres
- `ERR_EMAIL_DOMAIN_LENGTH_EXCEEDED`: Dominio excede 253 caracteres
- `ERR_EMAIL_INVALID_FORMAT`: Formato de email inválido
- `ERR_EMAIL_DOMAIN_INVALID_DASH`: Dominio con guiones inválidos
- `ERR_EMAIL_DOMAIN_CONSECUTIVE_DOTS`: Dominio con puntos consecutivos
- `ERR_USER_PASSWORD_HASH_NULL`: Hash de contraseña es null
- `ERR_USER_PASSWORD_HASH_EMPTY`: Hash de contraseña está vacío
- `ERR_USER_NAME_NULL`: Nombre de usuario es null
- `ERR_USER_NAME_EMPTY`: Nombre de usuario está vacío
- `ERR_USER_NAME_TOO_SHORT`: Nombre muy corto (< 3 caracteres)
- `ERR_USER_NAME_TOO_LONG`: Nombre muy largo (> 15 caracteres)
- `ERR_USER_INACTIVE`: Usuario inactivo

**Categoría:** Todos los errores son `TECNICO` (no errores de negocio).

**Severidad:** Varía según contexto (ERROR, WARNING, INFO).

---

## JUSTIFICACIÓN DE OUTCOME VS EXCEPTIONS

Este módulo utiliza **Outcome** porque:

1. **Login fallido NO es excepcional** - es un flujo de control esperado
2. **Múltiples intentos son normales** - necesitamos acumular información sin stack traces costosos
3. **Validaciones en cadena** - composición más natural que try-catch anidados
4. **Performance** - evitar overhead de excepciones en operaciones frecuentes (cada request)
5. **Información rica** - retornar múltiples warnings/infos sin el costo de crear excepciones
6. **Flujos alternativos válidos** - un usuario no verificado, bloqueado o inactivo son estados válidos del sistema, no errores

### Ejemplos de por qué Outcome es apropiado:

```java
// Login fallido - flujo esperado, no excepcional
public Outcome recordFailedLogin(Instant now, int maxAttempts, Duration lockDuration) {
    // Usuario no existe - no excepcional, puede ser typo
    // Password incorrecto - esperado, intentar de nuevo
    // Cuenta bloqueada - temporal, esperar
    // No verificado - flujo normal, enviar email
}

// Composición de validaciones - natural con Outcome
public Outcome canPerformSensitiveAction(Instant now) {
    if (!verified) return Outcome.fail(...);        // Común en usuarios nuevos
    if (isLocked(now)) return Outcome.fail(...);    // Recuperable después del timeout
    if (status != ACTIVE) return Outcome.fail(...); // Estado válido, solo no autorizado
    return Outcome.ok();
}
```

---

## INTEGRACIÓN CON MÓDULOS DE NEGOCIO

Cuando módulos de negocio (Patient, Appointment, etc.) necesitan validar usuarios:

**Problema:** Los módulos de negocio usan Exceptions, pero UserIdentity usa Outcome.

**Solución:** Domain Services traducen entre paradigmas:

```java
@Service
public class UserAccessValidator {
    
    // Consume Outcome (módulo técnico)
    public void validateUserCanPerformSensitiveAction(UserId userId, Instant now) {
        UserIdentity user = userRepo.findById(userId)...;
        
        Outcome eligibility = user.canPerformSensitiveAction(now);
        
        // Traduce a Exception (módulo negocio)
        if (!eligibility.isSuccess()) {
            throw new UserNotEligibleException(...);
        }
    }
}
```

---

## REGLAS DESCUBIERTAS (formato estandarizado)

**RN-USER-001**
- Descripción: El email debe ser único al crear usuario.
- Condición: `UserRepository.existsByEmail(email) == true` al invocar creación.
- Consecuencia: Se rechaza operación con Outcome.fail.
- Error asociado: `ERR_USER_DUPLICATE_EMAIL` (validado en capa de aplicación)
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-002**
- Descripción: La contraseña debe ser un `HashedPassword`.
- Condición: Password sin hash al invocar creación/edición.
- Consecuencia: Se rechaza en construcción de VO con Outcome.fail.
- Error asociado: `ERR_USER_PASSWORD_HASH_NULL` o `ERR_USER_PASSWORD_HASH_EMPTY`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-003**
- Descripción: Un usuario bloqueado no puede iniciar sesión.
- Condición: `user.isLocked(now) == true` al invocar login.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_ACCOUNT_LOCKED`
- Severidad: WARNING
- Categoría: TECNICO

**RN-USER-004**
- Descripción: Un usuario activo debe estar verificado para acciones sensibles.
- Condición: `user.verified == false` al invocar `canPerformSensitiveAction`.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_NOT_VERIFIED`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-005**
- Descripción: Solo puede editarse si pasa `canPerformSensitiveAction`.
- Condición: `canPerformSensitiveAction` retorna Outcome.fail al invocar `editUserData`.
- Consecuencia: Se propaga el Outcome.fail de la validación.
- Errores asociados: `ERR_USER_NOT_VERIFIED`, `ERR_USER_ACCOUNT_LOCKED`, `ERR_USER_INACTIVE`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-006**
- Descripción: El contador de intentos fallidos se reinicia solo en login exitoso.
- Condición: `recordSuccessfulLogin` ejecutado correctamente.
- Consecuencia: `failedLoginAttempts = 0` y actualización de `lastLoginAt`.
- Error asociado: N/A (regla de éxito)
- Categoría: TECNICO

**RN-USER-007**
- Descripción: La eliminación física está prohibida.
- Condición: `delete(user)` en repositorio.
- Consecuencia: Se rechaza en capa de aplicación (no en agregado).
- Error asociado: `ERR_USER_PHYSICAL_DELETE_NOT_ALLOWED`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-008**
- Descripción: Usuario solo puede verificarse una vez.
- Condición: `verified == true` al invocar `verify()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_NOT_VERIFIED`
- Severidad: INFO
- Categoría: TECNICO

**RN-USER-009**
- Descripción: Intentos fallidos incrementan hasta bloquear cuenta.
- Condición: `failedLoginAttempts >= maxAttempts` al invocar `recordFailedLogin`.
- Consecuencia: Se establece `lockedUntil` y se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_ACCOUNT_LOCKED_DUE_TO_FAILED_ATTEMPTS`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-010**
- Descripción: Usuario solo puede estar en estados válidos.
- Condición: Transición inválida en `UserStatus.canTransitionTo()`.
- Consecuencia: Se rechaza transición (implementado en capa de aplicación).
- Errores asociados: Específicos según transición inválida
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-011**
- Descripción: Email debe cumplir formato válido.
- Condición: Email no cumple validaciones en `Email.of()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Errores asociados: Ver sección "Validaciones de Value Objects - Email"
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-012**
- Descripción: UserName debe cumplir restricciones de longitud.
- Condición: Nombre < 3 o > 15 caracteres en `UserName.create()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Errores asociados: `ERR_USER_NAME_TOO_SHORT`, `ERR_USER_NAME_TOO_LONG`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-013**
- Descripción: Suspensión requiere razón obligatoria.
- Condición: `reason == null || reason.isBlank()` al invocar `suspend()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_SUSPENSION_REQUIRES_REASON`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-014**
- Descripción: Usuario debe estar verificado para reactivarse.
- Condición: `verified == false` al invocar `reactivate()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_NOT_VERIFIED`
- Severidad: ERROR
- Categoría: TECNICO

**RN-USER-015**
- Descripción: Reactivación limpia bloqueos y contador de intentos.
- Condición: `reactivate()` ejecutado correctamente.
- Consecuencia: `failedLoginAttempts = 0`, `lockedUntil = null`, `status = ACTIVE`.
- Error asociado: N/A (regla de éxito)
- Categoría: TECNICO

**RN-USER-016**
- Descripción: Desactivación requiere cumplir política de desactivación.
- Condición: `policy.canDeactivate(this) == false` al invocar `deactivate()`.
- Consecuencia: Se rechaza con Outcome.fail.
- Error asociado: `ERR_USER_DEACTIVATION_CONSTRAINTS`
- Severidad: ERROR
- Categoría: TECNICO

---

## RELACIÓN CON ADRs
- **ADR-0010**: Cambios en el módulo de acceso y diseño del agregado `UserIdentity`.
- **ADR-0011**: Estrategia Híbrida de Manejo de Errores - Outcome para Módulos Técnicos.
- ADR-032: Implementación sistemática de reglas de negocio por agregado.
- ADR-034: Guardian de reglas de negocio - validación de autenticación.

---

## EVENTOS DE DOMINIO
- `UserRegistered` → Al crear nuevo usuario.
- `UserLoggedIn` → Al registrar login exitoso.
- `UserLocked` → Al bloquear usuario por intentos fallidos.
- `UserVerified` → Al verificar cuenta.
- `UserProfileUpdated` → Al editar datos sensibles.
- `UserDeactivated` → Al cambiar estado a inactivo.
- `UserSuspended` → Al suspender usuario.
- `UserReactivated` → Al reactivar usuario.

---

## NOTAS DE IMPLEMENTACIÓN

### Optimistic Locking
- El agregado incluye campo `version` para control de concurrencia.
- JPA maneja automáticamente con `@Version`.
- Previene condiciones de carrera en operaciones concurrentes.

### Responsabilidades por Capa

**Dominio (UserIdentity):**
- Mantener invariantes de negocio
- Validar reglas de acceso y seguridad
- Retornar Outcome para todos los flujos

**Aplicación:**
- Orquestar flujo de autenticación
- Validar unicidad de email (repositorio)
- Traducir Outcome a eventos de dominio
- Integrar con PasswordEncoder (infra)

**Infraestructura:**
- PasswordEncoder para hashing
- Filtros JWT y Spring Security
- Constraints de BD (unique email)
- Gestión de sesiones y tokens

### Testing

**Unit Tests del Dominio:**
- `recordFailedLogin` incrementa contador y bloquea tras N intentos
- `recordSuccessfulLogin` resetea contador y actualiza `lastLoginAt`
- `canPerformSensitiveAction` valida condiciones de elegibilidad
- Validaciones de VOs (Email, UserName, HashedPassword)
- Transiciones de estado válidas e inválidas

**Integration Tests:**
- Flujo completo de login (exitoso/fallido)
- Bloqueo por intentos y desbloqueo automático
- Verificación de cuenta
- Edición de perfil con validaciones
- Integración con JWT y Spring Security