# ADR-04 (User): Validación de usuarios con UserAccessValidator

- **Fecha**: 2026-01-28
- **Estado**: Aprobado
- **Categoría**: Dominio
- **Depreca**: ADR-001 (desactivación en agregado)
- **Motivado por**: ADR-02 (UserIdentity rico con lógica compleja)

---

## Problema

Con la refactorización de UserIdentity como agregado rico (ADR-02), surgió acoplamiento problemático entre agregados de negocio (Patient, Dentist, Guardian) y el agregado técnico UserIdentity.

### Situación problemática

Los agregados del módulo Actor validaban directamente el estado del usuario:

```java
// Patient.java - acoplamiento directo
public static Patient registerPatient(
    Person data,
    UserIdentity user,      // ← Agregado completo
    GuardianId guardian
) {
    // Validación problemática
    UserStatus.from(user, Instant.now()).mustBeActive(...);
    
    return new Patient(..., user.getId(), ...);
}
```

### Problemas identificados

**1. Desincronización de estado**
```java
// UserStatus calcula estado derivado
UserStatus.from(user, now) // ← Basado en canPerformSensitiveAction()

// Pero UserIdentity almacena estado persistido
user.getStatus() // ← Field status

// ¿Cuál es la fuente de verdad?
```

**2. Violación de límites del agregado**
```java
Patient → UserStatus.from(user, now) 
              → user.canPerformSensitiveAction()
              → user.isVerified()
              → user.isLocked()
```
Patient navegaba dentro del agregado UserIdentity, violando encapsulamiento.

**3. Mezcla de paradigmas de error**
```java
// UserIdentity usa Outcome (técnico)
public Outcome canPerformSensitiveAction(Instant now) { ... }

// Patient usa Exceptions (negocio)
UserStatus.from(user, now).mustBeActive(...) // ← Lanza exception
```

**4. Duplicación en 4 agregados**

Validación problemática repetida en:
- `Patient.registerPatient()`
- `Dentist.registerDentist()`
- `Guardian.register()`
- `Receptionist.register()`

## Decisión

Extraer la validación de acceso de usuarios a un **Domain Service** dedicado: `UserAccessValidator`.

### Arquitectura de la solución

```
┌──────────────────────────────────────────────────────────┐
│  ANTES (Problemático)                                    │
│                                                          │
│  Patient.registerPatient(user)                          │
│       │                                                  │
│       └─→ UserStatus.from(user, now).mustBeActive()    │
│                │                                         │
│                └─→ user.canPerformSensitiveAction()     │
│                         (navega dentro del agregado)    │
│                                                          │
│  ❌ Acoplamiento Patient → UserIdentity                 │
│  ❌ Violación de límites                                │
│  ❌ Mezcla Outcome/Exception                            │
└──────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────┐
│  DESPUÉS (Solución)                                      │
│                                                          │
│  RegisterPatientUseCase.execute(command)                │
│       │                                                  │
│       ├─→ UserAccessValidator.validate(userId, now)    │
│       │        │                                         │
│       │        ├─→ userRepo.findById(userId)           │
│       │        ├─→ user.canPerformSensitiveAction()    │
│       │        └─→ translate Outcome → Exception        │
│       │                                                  │
│       └─→ Patient.registerPatient(userId)              │
│                  (solo recibe ID)                        │
│                                                          │
│  ✅ Patient desacoplado de UserIdentity                 │
│  ✅ Validación centralizada                             │
│  ✅ Anti-corruption layer                               │
└──────────────────────────────────────────────────────────┘
```

## Implementación

### Domain Service

```java
@Service
public class UserAccessValidator {
    
    private final UserIdentityRepository userRepository;
    
    /**
     * Valida que un usuario puede realizar acciones sensibles.
     * 
     * ÚNICA forma correcta de validar usuarios desde módulos
     * de negocio (Patient, Dentist, Guardian, Receptionist).
     * 
     * Responsabilidades:
     * 1. Obtener UserIdentity desde repositorio
     * 2. Invocar canPerformSensitiveAction() → Outcome
     * 3. Traducir a Exception si falla (Anti-Corruption Layer)
     * 4. Proveer contexto del agregado solicitante
     */
    public void validateUserCanPerformSensitiveAction(
        UserId userId,
        Instant now,
        EntityContext requesterContext
    ) {
        // 1. Obtener usuario
        UserIdentity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(
                userId, 
                requesterContext
            ));
        
        // 2. Validar (Outcome del módulo técnico)
        Outcome<Void> eligibility = user.canPerformSensitiveAction(now);
        
        // 3. Traducir Outcome → Exception (para módulos de negocio)
        if (!eligibility.isSuccess()) {
            throw translateToBusinessException(
                userId,
                eligibility.getDetails(),
                requesterContext
            );
        }
    }
    
    private UserNotEligibleException translateToBusinessException(
        UserId userId,
        List<OutcomeDetail> details,
        EntityContext context
    ) {
        // Mapear errores técnicos a errores de negocio
        OutcomeDetail firstError = details.get(0);
        
        return switch (firstError.getCode()) {
            case UserIdentityError.ERR_USER_NOT_VERIFIED ->
                new UserNotEligibleException(
                    "User not verified",
                    context
                );
            case UserIdentityError.ERR_USER_ACCOUNT_LOCKED ->
                new UserNotEligibleException(
                    "User account locked",
                    context
                );
            case UserIdentityError.ERR_USER_INACTIVE ->
                new UserNotEligibleException(
                    "User inactive",
                    context
                );
            default ->
                new UserNotEligibleException(
                    "User not eligible",
                    context
                );
        };
    }
}
```

### Agregado refactorizado

**Antes:**
```java
public static Patient registerPatient(
    Person data,
    UserIdentity user,      // ← Agregado completo
    GuardianId guardian
) {
    UserStatus.from(user, Instant.now()).mustBeActive(...);
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
    // NO valida usuario - es responsabilidad del use case
    
    if (!data.getAge().isEligibleForRegistration()) {
        throw new BusinessRuleViolationException(...);
    }
    
    return new Patient(..., userId, ...);
}
```

### Application Service

```java
@Service
@Transactional
public class RegisterPatientUseCase {
    
    private final PatientRepository patientRepository;
    private final UserAccessValidator userAccessValidator;
    
    public PatientDto execute(RegisterPatientCommand command) {
        Instant now = Instant.now();
        
        // 1. Validar acceso del usuario (domain service)
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

## Justificación

### ¿Por qué un Domain Service y no validación en el agregado?

#### 1. Límites de agregado (DDD)

Según DDD, los agregados solo deben tomar decisiones basadas en:
- Su propio estado interno
- Referencias a otros agregados (solo IDs)
- **NO** el estado completo de otros agregados

```java
// INCORRECTO
Patient.registerPatient(user) {
    if (!user.isActive()) throw ... // ← Violación
}

// CORRECTO
RegisterPatientUseCase.execute(cmd) {
    validator.validate(userId);      // ← Coordina entre agregados
    Patient.registerPatient(userId); // ← Solo reglas propias
}
```

#### 2. Evitar race conditions

```java
// Sin Domain Service:
// Thread 1:
UserIdentity user = userRepo.findById(userId);
if (!user.canPerformSensitiveAction()) throw... // ✅ OK

// Thread 2: (entre líneas anteriores)
user.deactivate(); // ❌ Desactiva
userRepo.save(user);

// Thread 1: continúa
new Patient(..., user.getId(), ...) // 💥 Paciente con usuario inactivo

// Con Domain Service + @Transactional:
@Transactional // ← Lock para toda la operación
RegisterPatientUseCase.execute() {
    validator.validate(userId);    // ← Lee con lock
    Patient.register(userId);      // ← Crea con lock
    patientRepo.save(patient);     // ← Guarda con lock
} // COMMIT - todo atómico
```

#### 3. Centralización vs duplicación

**Sin Domain Service (antes):**
```java
Patient.registerPatient()        → validación
Patient.updateContactData()      → validación
Dentist.registerDentist()        → validación
Guardian.register()              → validación
Receptionist.register()          → validación
```

**Con Domain Service (ahora):**
```java
PatientUseCase      → userAccessValidator.validate()
DentistUseCase      → userAccessValidator.validate()
GuardianUseCase     → userAccessValidator.validate()
ReceptionistUseCase → userAccessValidator.validate()
```

Un solo lugar para lógica, traducción, tests.

#### 4. Anti-Corruption Layer

El Domain Service actúa como **capa anti-corrupción** entre:

**Módulo Técnico (UserIdentity):**
- Usa `Outcome` para control de flujo
- Lógica de autenticación/autorización

**Módulos de Negocio (Patient, Dentist, etc.):**
- Usan `Exceptions` para reglas de negocio
- Lógica clínica/administrativa

```java
UserAccessValidator traduce:
Outcome (UserIdentity) → Exception (Patient, Dentist, etc.)
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Validación en cada agregado | Duplicación en 4+ agregados, difícil mantener consistencia |
| Método estático compartido | No tiene acceso a repositorio, sigue siendo acoplamiento |
| Validar en controlador REST | Lógica de negocio no pertenece a capa de presentación |

## Consecuencias

### Ganamos
- **Separación de concerns:** Patient solo maneja reglas de paciente
- **Reutilización:** 4 agregados usan el mismo validator
- **Consistencia transaccional:** `@Transactional` garantiza atomicidad
- **Testabilidad:** Mockear validator en tests unitarios
- **Mantenibilidad:** Cambios solo tocan 1 archivo
- **Anti-corruption:** Traduce `Outcome` → `Exception` apropiadamente

### Perdemos
- **Más código:** Domain Service + configuración + tests
- **Disciplina requerida:** Recordar usar validator en use cases
- **Posible olvido:** Si alguien crea agregado sin usar validator

## Mitigación de riesgos

### Prevenir bypass del validator

**Arquitectest (ArchUnit):**
```java
@ArchTest
static final ArchRule aggregates_must_not_receive_UserIdentity =
    methods()
        .that().areDeclaredInClassesThat()
            .resideInAPackage("..domain..")
        .and().arePublic()
        .should().notHaveRawParameterTypes(UserIdentity.class)
        .because("Aggregates must only receive UserId, not full UserIdentity");
```

**Code review checklist:**
- [ ] Use case valida usuario con `UserAccessValidator`
- [ ] Agregado recibe `UserId`, no `UserIdentity`
- [ ] Método está en transacción

## Relación con otros ADRs

- **Depreca:**
    - ADR-001: Desactivación de usuario en agregado

- **Motivado por:**
    - ADR-021: UserIdentity como agregado rico

- **Complementa:**
    - ADR-008: UserDeactivationPolicy (valida restricciones de desactivación)
    - ADR-022: Spring Security integración (maneja autenticación técnica)

- **Referencia:**
    - Ver patrón general en `/docs/aprendizajes/validaciones-cruzadas-agregados.md`