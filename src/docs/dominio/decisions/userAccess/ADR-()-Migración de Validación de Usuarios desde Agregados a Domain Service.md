# ADR 003: Migración de Validación de Usuarios desde Agregados a Domain Service

**Fecha:** 28/01/2026  
**Estado:** Aprobado  
**Contexto:** Refactorización del módulo de acceso y sus dependencias

---

## Contexto

### Situación Anterior

Inicialmente, los agregados del módulo de actores (Patient, Dentist, Guardian, Receptionist) validaban directamente el estado del usuario asociado:

```java
// Patient.java - implementación original
public static Patient registerPatient(
    Person data,
    UserIdentity user,
    GuardianId guardian
) {
    // ... validaciones de paciente ...
    
    // Validación de usuario - PROBLEMÁTICA
    UserStatus.from(user, Instant.now()).mustBeActive(
        ErrorCatalog.ERR_PATIENT_INACTIVE,
        EntityContext.PATIENT
    );
    
    return new Patient(..., user.getId(), ...);
}
```

Este enfoque tenía sentido cuando `UserIdentity` era un agregado simple que solo mantenía estado básico (email, password, status). La validación era directa: ¿el usuario está activo?

### El Punto de Inflexión: Refactorización de UserIdentity

Como parte de ADR-002, refactorizamos `UserIdentity` para convertirlo en un agregado rico con lógica técnica compleja:

**Nuevas responsabilidades agregadas:**
1. **Verificación de cuenta** (`verified` flag)
2. **Bloqueo por intentos fallidos** (`failedLoginAttempts`, `lockedUntil`)
3. **Validaciones temporales** (bloqueos que expiran)
4. **Composición de condiciones** (activo + verificado + no bloqueado)

```java
// UserIdentity.java - después de refactorización
public Outcome canPerformSensitiveAction(Instant now) {
    if (!verified) {
        return Outcome.fail(new OutcomeDetail(
            UserIdentityError.ERR_USER_NOT_VERIFIED,
            Severity.ERROR,
            Category.TECNICO
        ));
    }
    
    if (isLocked(now)) {
        return Outcome.fail(new OutcomeDetail(
            UserIdentityError.ERR_USER_ACCOUNT_LOCKED,
            Severity.ERROR,
            Category.TECNICO
        ));
    }
    
    if (status.getState() != UserStatus.State.ACTIVE) {
        return Outcome.fail(new OutcomeDetail(
            UserIdentityError.ERR_USER_INACTIVE,
            Severity.ERROR,
            Category.TECNICO
        ));
    }
    
    return Outcome.ok();
}
```

### El Problema que Surgió

La complejidad añadida a `UserIdentity` rompió el acoplamiento existente:

#### 1. Desincronización de Estado
```java
// Patient consulta:
UserStatus.from(user, Instant.now()) // ← Calcula estado derivado

// Pero UserIdentity almacena:
this.status = UserStatus.of(State.ACTIVE) // ← Estado persistido

// ¿Cuál es la fuente de verdad? 🤔
```

El `UserStatus.from(user, now)` calculaba un estado derivado basado en `canPerformSensitiveAction()`, pero este estado podía no coincidir con el `status` almacenado en el agregado.

**Caso real que encontramos:**
```java
// Usuario está ACTIVE según su field status
user.getStatus().getState() == ACTIVE // true

// Pero canPerformSensitiveAction() falla porque:
user.isVerified() == false // ← No verificado
user.isLocked(now) == true // ← Bloqueado temporalmente

// UserStatus.from(user, now) retorna INACTIVE
// Pero user.status.getState() sigue siendo ACTIVE
// ¡Inconsistencia! 💥
```

#### 2. Violación de Límites del Agregado

```java
// Patient conoce demasiado sobre UserIdentity
Patient → UserStatus.from(user, now) → user.canPerformSensitiveAction()
                                      → user.isVerified()
                                      → user.isLocked()
                                      → user.getStatus()
```

Patient estaba **navegando** dentro del agregado UserIdentity a través de UserStatus, violando el principio de que los agregados solo deberían conocer IDs de otros agregados.

#### 3. Cambio de Paradigma de Errores

```java
// UserIdentity ahora usa Outcome (ADR-0011)
public Outcome canPerformSensitiveAction(Instant now) { ... }

// Pero Patient usa Exceptions
UserStatus.from(user, now).mustBeActive(...) // ← Lanza exception

// El código tiene que traducir entre paradigmas
// de forma implícita y propensa a errores
```

#### 4. Comparaciones Incorrectas

```java
// En editUserData() - código original
if (!Objects.equals(this.status, UserStatus.from(UserStatus.State.ACTIVE))) {
    // Este equals() SIEMPRE es false porque:
    // 1. UserStatus no implementaba equals/hashCode
    // 2. Cada from() crea una NUEVA instancia
    // 3. Objects.equals() compara referencias, no valores
}
```

Esto causaba que validaciones fallaran silenciosamente o pasaran cuando no deberían.

#### 5. Duplicación en 4 Agregados

Teníamos esta validación problemática duplicada en:
- Patient.registerPatient()
- Doctor.registerDoctor()
- Appointment.schedule()
- MedicalRecord.create()

Y cada futura feature que necesitara validar usuarios tendría que repetir este patrón incorrecto.

---

## Decisión

**Extraer la validación de acceso de usuarios a un Domain Service dedicado: `UserAccessValidator`.**

### Arquitectura de la Solución

```
┌─────────────────────────────────────────────────────────────┐
│              ANTES (Problemático)                            │
│                                                              │
│  Patient.registerPatient(user)                              │
│       │                                                      │
│       └─→ UserStatus.from(user, now).mustBeActive()        │
│                    │                                        │
│                    └─→ user.canPerformSensitiveAction()    │
│                              │                              │
│                              ├─ user.isVerified()           │
│                              ├─ user.isLocked()             │
│                              └─ user.getStatus()            │
│                                                              │
│  Problemas:                                                  │
│  ❌ Acoplamiento directo Patient → UserIdentity             │
│  ❌ Violación de límites del agregado                       │
│  ❌ Estado derivado vs estado persistido                    │
│  ❌ Mixing Outcome (UserIdentity) con Exceptions (Patient)  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              DESPUÉS (Solución)                              │
│                                                              │
│  RegisterPatientUseCase.execute(command)                    │
│       │                                                      │
│       ├─→ UserAccessValidator.validate(userId, now)        │
│       │        │                                            │
│       │        ├─→ userRepo.findById(userId)               │
│       │        │                                            │
│       │        ├─→ user.canPerformSensitiveAction(now)     │
│       │        │        (Outcome)                           │
│       │        │                                            │
│       │        └─→ translate Outcome → Exception           │
│       │                 (Anti-Corruption Layer)             │
│       │                                                      │
│       └─→ Patient.registerPatient(userId)                  │
│                  (Solo recibe ID, sin validación)           │
│                                                              │
│  Beneficios:                                                 │
│  ✅ Patient desacoplado de UserIdentity                     │
│  ✅ Validación centralizada en 1 lugar                      │
│  ✅ Traducción Outcome → Exception en capa apropiada        │
│  ✅ Patient solo conoce UserId, no el agregado completo     │
└─────────────────────────────────────────────────────────────┘
```

### Implementación del Domain Service

```java
@Service
public class UserAccessValidator {
    
    private final UserIdentityRepository userRepository;
    
    /**
     * Valida que un usuario puede realizar acciones sensibles.
     * 
     * Esta es la ÚNICA forma correcta de validar usuarios desde
     * módulos de negocio (Patient, Doctor, etc.).
     * 
     * Responsabilidades:
     * 1. Obtener UserIdentity desde repositorio
     * 2. Invocar canPerformSensitiveAction() (Outcome)
     * 3. Traducir a Exception si falla (Anti-Corruption Layer)
     * 4. Proporcionar contexto específico del agregado solicitante
     */
    public void validateUserCanPerformSensitiveAction(
        UserId userId,
        Instant now,
        EntityContext requesterContext
    ) {
        // 1. Obtener usuario
        UserIdentity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId, requesterContext));
        
        // 2. Validar (retorna Outcome del módulo técnico)
        Outcome eligibility = user.canPerformSensitiveAction(now);
        
        // 3. Traducir Outcome → Exception (para módulos de negocio)
        if (!eligibility.isSuccess()) {
            throw translateToBusinessException(
                userId,
                eligibility.getDetalles(),
                requesterContext
            );
        }
    }
    
    private UserNotEligibleException translateToBusinessException(...) {
        // Extrae información del Outcome y crea Exception apropiada
        // con contexto específico del agregado solicitante
    }
}
```

### Refactorización de Agregados

**Antes:**
```java
public static Patient registerPatient(
    Person data,
    UserIdentity user,      // ← Agregado completo
    GuardianId guardian
) {
    UserStatus.from(user, Instant.now()).mustBeActive(...); // ← Validación interna
    return new Patient(..., user.getId(), ...);
}
```

**Después:**
```java
public static Patient registerPatient(
    Person data,
    UserId userId,          // ← Solo ID
    GuardianId guardian
) {
    // NO valida usuario aquí - es responsabilidad del use case
    
    if (!data.getAge().isEligibleForRegistration()) {
        throw new DomainAggregateException(...);
    }
    
    return new Patient(..., userId, ...);
}
```

### Orquestación en Servicios de Aplicación

```java
@Service
@Transactional
public class RegisterPatientUseCase {
    
    private final PatientRepository patientRepository;
    private final UserAccessValidator userAccessValidator;
    
    public PatientDTO execute(RegisterPatientCommand command) {
        Instant now = Instant.now();
        
        // 1. Validar usuario (delega a domain service)
        userAccessValidator.validateUserCanPerformSensitiveAction(
            command.getUserId(),
            now,
            EntityContext.PATIENT
        );
        
        // 2. Crear agregado (solo reglas propias)
        Patient patient = Patient.registerPatient(
            command.getPersonalData(),
            command.getUserId(),
            command.getGuardianId()
        );
        
        // 3. Persistir
        return patientRepository.save(patient);
    }
}
```

---

## Razonamiento

### ¿Por qué un Domain Service y no validación en el agregado?

#### 1. Consistencia entre Agregados (DDD)

Según DDD, los agregados solo deberían tomar decisiones basadas en:
- Su propio estado interno
- Referencias a otros agregados (solo IDs)
- **NO** el estado completo de otros agregados

```java
// INCORRECTO - Patient toma decisión basada en estado de UserIdentity
Patient.registerPatient(user) {
    if (!user.isActive()) throw ... // ← Violación
}

// CORRECTO - Servicio de aplicación orquesta
RegisterPatientUseCase.execute(cmd) {
    validator.validate(userId);     // ← Coordina entre agregados
    Patient.registerPatient(userId); // ← Solo reglas propias
}
```

#### 2. Evitar Race Conditions

```java
// Escenario problemático sin Domain Service:
// Thread 1: Patient.registerPatient()
UserIdentity user = userRepo.findById(userId);
if (!user.canPerformSensitiveAction()) throw... // ✅ Usuario activo

// Thread 2: (entre las líneas anteriores)
user.deactivate(); // ❌ Usuario se desactiva
userRepo.save(user);

// Thread 1: continúa
new Patient(..., user.getId(), ...) // 💥 Paciente con usuario inactivo

// Con Domain Service + @Transactional:
@Transactional // ← Lock para toda la operación
RegisterPatientUseCase.execute() {
    validator.validate(userId);    // ← Lee con lock
    Patient.register(userId);      // ← Crea con lock
    patientRepo.save(patient);     // ← Guarda con lock
    // COMMIT - todo atómico
}
```

#### 3. Centralización vs Duplicación

**Sin Domain Service:**
```java
Patient.registerPatient()     → UserStatus.from(user, now).mustBeActive()
Patient.UpdateContactData()     → UserStatus.from(user, now).mustBeActive()
Patient.UpdateSensitiveData()     → UserStatus.from(user, now).mustBeActive()
Dentist.registerDentist()     → UserStatus.from(user, now).mustBeActive()
Dentist.udateContactData()     → UserStatus.from(user, now).mustBeActive()
Dentist.updateSensitiveData()     → UserStatus.from(user, now).mustBeActive()
Guardian.register()     → UserStatus.from(user, now).mustBeActive()
Receptionist.register()     → UserStatus.from(user, now).mustBeActive()
    

```

**Con Domain Service:**
```java
PatientUseCase     → userAccessValidator.validate()
DentistUseCase      → userAccessValidator.validate()
GuardianUseCase     → userAccessValidator.validate()
ReceptionistUseCase       → userAccessValidator.validate()

// Un solo lugar para:
// - Lógica de validación
// - Traducción Outcome → Exception
// - Manejo de casos edge
// - Tests
```

#### 4. Anti-Corruption Layer

El Domain Service actúa como **capa anti-corrupción** entre:

**Módulo Técnico (UserIdentity):**
- Usa Outcome para control de flujo
- Lógica de autenticación/autorización
- Performance crítica

**Módulos de Negocio (Patient, Dentist, Guardian, Receptionist.):**
- Usan Exceptions para reglas de negocio
- Lógica clínica/administrativa
- Claridad semántica

```java
// UserAccessValidator traduce entre mundos
Outcome (UserIdentity) → Exception (Patient,Dentist, Guardian, Receptionist)
```

---

## Consecuencias

### Positivas

✅ **Separación de concerns:** Patient solo maneja reglas de paciente, no de usuarios

✅ **Reutilización:** 4 agregados usan el mismo validator, futuros agregados también

✅ **Consistencia transaccional:** @Transactional en use case garantiza atomicidad

✅ **Testabilidad:** Podemos mockear validator fácilmente en tests unitarios

✅ **Mantenibilidad:** Cambios en lógica de validación solo tocan 1 archivo

✅ **Claridad:** Queda explícito que la validación es entre bounded contexts

✅ **Anti-corruption:** Traduce Outcome → Exception en la capa apropiada

### Negativas

⚠️ **Más código:** Domain Service + configuración Spring + tests adicionales

⚠️ **Capa extra:** Desarrolladores deben recordar usar validator en use cases

⚠️ **Posible olvido:** Si alguien crea agregado sin usar validator



## Referencias

- ADR-002: Cambios en el módulo de acceso y diseño del agregado UserIdentity
- ADR-034: Estrategia híbrida de manejo de errores (Outcome vs Exceptions)
- DDD Reference: Aggregate boundaries and consistency
- Implementation: `domain/service/UserAccessValidator.java`

---