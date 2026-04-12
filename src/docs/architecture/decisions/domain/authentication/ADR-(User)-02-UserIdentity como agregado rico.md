# ADR-02 (User): UserIdentity como agregado rico

- **Fecha**: 2026-01-21
- **Estado**: Aprobado
- **Categoría**: Dominio
- **Autor:** David Stiven Sanclemente

---

## Problema

En la arquitectura MVC original, `UserIdentity` era un POJO técnico sin lógica de negocio:

```java
// UserIdentity original (POJO)
public class UserIdentity {
    private Long id;
    private String email;
    private String hashedPassword;
    private UserStatus status;
    
    // Solo getters y setters
}
```

**Problemas:**
- Reglas de negocio dispersas entre controladores, servicios y filtros
- Posibilidad de crear estados inconsistentes (activo pero no verificado)
- No hay punto único de validación
- Difícil de testear reglas de negocio

**Necesidades del negocio:**
- Bloqueo automático por intentos fallidos de login
- Verificación obligatoria de cuenta antes de acciones sensibles
- Trazabilidad de último login
- Validación de estado antes de operaciones críticas

## Decisión

Convertir `UserIdentity` en **agregado rico** que encapsula invariantes de negocio y comportamiento.

### Invariantes del agregado

1. **Usuario activo ≠ usuario elegible**
    - Un usuario puede estar ACTIVE pero no verificado → no puede operar
    - Un usuario puede estar ACTIVE pero bloqueado → no puede operar

2. **Bloqueo por intentos fallidos**
    - Después de N intentos fallidos (configurable), el usuario se bloquea temporalmente
    - El bloqueo expira automáticamente después de X minutos

3. **Verificación obligatoria**
    - El flag `verified` debe ser true para acciones sensibles
    - Solo se puede verificar una vez

4. **Estado derivado vs estado persistido**
    - `status` es el estado persistido (ACTIVE/INACTIVE)
    - La elegibilidad real se calcula con `canPerformSensitiveAction()`

### Diseño del agregado

```java
public class UserIdentity {
    // Identity
    private final UserId id;
    private final Email email;
    
    // Security
    private HashedPassword hashedPassword;
    private boolean verified;
    private int failedLoginAttempts;
    private Instant lockedUntil;
    
    // State
    private UserStatus status;  // ACTIVE, INACTIVE
    private Set<Role> roles;
    
    // Audit
    private Instant createdAt;
    private Instant lastLoginAt;
    private Long version;  // Optimistic locking
    
    // Constructor privado - solo crear via factory methods
    private UserIdentity(...) { }
    
    // Factory method
    public static UserIdentity create(
        Email email,
        HashedPassword password,
        Set<Role> roles
    ) {
        return new UserIdentity(
            UserId.generate(),
            email,
            password,
            false,  // no verificado inicialmente
            0,      // sin intentos fallidos
            null,   // sin bloqueo
            UserStatus.ACTIVE,
            roles,
            Instant.now(),
            null,
            0L
        );
    }
    
    // Regla de negocio principal
    public Outcome<Void> canPerformSensitiveAction(Instant now) {
        // 1. Verificado?
        if (!verified) {
            return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_NOT_VERIFIED,
                Severity.ERROR,
                Category.TECNICO
            ));
        }
        
        // 2. Bloqueado?
        if (isLocked(now)) {
            return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
                Severity.ERROR,
                Category.TECNICO
            ));
        }
        
        // 3. Activo?
        if (status != UserStatus.ACTIVE) {
            return Outcome.fail(new OutcomeDetail(
                UserIdentityError.ERR_USER_INACTIVE,
                Severity.ERROR,
                Category.TECNICO
            ));
        }
        
        return Outcome.ok();
    }
    
    // Comportamiento: registrar intento fallido
    public void recordFailedLogin(Instant now) {
        failedLoginAttempts++;
        
        if (failedLoginAttempts >= MAX_ATTEMPTS) {
            lockedUntil = now.plus(LOCK_DURATION_MINUTES, ChronoUnit.MINUTES);
        }
    }
    
    // Comportamiento: registrar login exitoso
    public void recordSuccessfulLogin(Instant now) {
        failedLoginAttempts = 0;
        lockedUntil = null;
        lastLoginAt = now;
    }
    
    // Comportamiento: verificar cuenta
    public void verify() {
        if (verified) {
            throw new BusinessRuleViolationException(
                "User already verified"
            );
        }
        verified = true;
    }
    
    // Comportamiento: desactivar
    public void deactivate() {
        if (status == UserStatus.INACTIVE) {
            throw new BusinessRuleViolationException(
                "User already inactive"
            );
        }
        status = UserStatus.INACTIVE;
    }
    
    // Consulta: está bloqueado?
    public boolean isLocked(Instant now) {
        return lockedUntil != null && now.isBefore(lockedUntil);
    }
    
    // Getters (sin setters para invariantes)
    public UserId getId() { return id; }
    public Email getEmail() { return email; }
    public boolean isVerified() { return verified; }
    public UserStatus getStatus() { return status; }
    public Set<Role> getRoles() { return Set.copyOf(roles); }
    public Instant getLastLoginAt() { return lastLoginAt; }
}
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Dejar como POJO y validar en servicios | Reglas dispersas, fácil violar invariantes |
| Mover todo a Spring Security | Pierde modelado de dominio, difícil testear lógica de negocio |
| Agregar setters públicos | Permite crear estados inconsistentes |

## Consecuencias

### Ganamos
- **Invariantes protegidas:** Imposible crear estados inconsistentes
- **Testabilidad:** Pruebas unitarias sin infraestructura
  ```java
  @Test
  void testLoginFailureLocking() {
      UserIdentity user = UserIdentity.create(...);
      Instant now = Instant.now();
      
      // 3 intentos fallidos
      user.recordFailedLogin(now);
      user.recordFailedLogin(now);
      user.recordFailedLogin(now);
      
      // Usuario bloqueado
      assertTrue(user.isLocked(now));
      
      // No puede operar
      Outcome result = user.canPerformSensitiveAction(now);
      assertFalse(result.isSuccess());
  }
  ```

- **Comportamiento centralizado:** Toda la lógica de login en un lugar
- **Trazabilidad:** Estado derivado calculado consistentemente
- **Código expresivo:** `user.recordFailedLogin()` vs `user.setFailedAttempts(user.getFailedAttempts() + 1)`

### Perdemos
- **Más código inicial:** Métodos en lugar de setters
- **Requiere disciplina:** No bypass con setters
- **Coordinación necesaria:** Optimistic locking con `@Version`

## Value Objects utilizados

```java
// UserId
public record UserId(UUID value) {
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}

// Email
public record Email(String value) {
    public Email {
        if (!value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

// HashedPassword
public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.length() < 60) {
            throw new IllegalArgumentException("Invalid hashed password");
        }
    }
}

// Role
public enum Role {
    ADMIN,
    DENTIST,
    PATIENT,
    GUARDIAN,
    RECEPTIONIST
}
```

## Optimistic Locking

Para evitar condiciones de carrera en actualizaciones concurrentes:

```java
@Entity
@Table(name = "user_identity")
public class UserIdentityEntity {
    @Id
    private UUID id;
    
    @Version  // JPA optimistic locking
    private Long version;
    
    // ...
}
```

**Escenario protegido:**
```
Thread 1: user.recordFailedLogin()  → version = 1
Thread 2: user.recordFailedLogin()  → version = 1

Thread 1: repository.save(user)     → version = 2 ✅
Thread 2: repository.save(user)     → OptimisticLockException ❌

Thread 2 debe recargar y reintentar
```


## Relación con otros ADRs

- [ADR-(Arquitectura)-38-UserDeactivationPolicy como orquestador de validaciones.md](../../arch/ADR-%28Arquitectura%29-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md)
- [ADR-(Arquitectura)-37-Arquitectura hexagonal para módulo de acceso.md](../../arch/ADR-%28Arquitectura%29-37-Arquitectura%20hexagonal%20para%20m%C3%B3dulo%20de%20acceso.md)
- [ADR-(Arquitectura)-39-Ubicación de validaciones de desactivación.md](../../arch/ADR-%28Arquitectura%29-39-Ubicaci%C3%B3n%20de%20validaciones%20de%20desactivaci%C3%B3n.md)
- [ADR‑(Arquitectura)-46-Integración JWT y Spring Security en arquitectura hexagonal.md](../../arch/ADR%E2%80%91%28Arquitectura%29-46-Integraci%C3%B3n%20JWT%20y%20Spring%20Security%20en%20arquitectura%20hexagonal.md)