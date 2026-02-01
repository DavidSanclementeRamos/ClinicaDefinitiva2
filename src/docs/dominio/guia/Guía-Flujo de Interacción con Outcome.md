# Guía: Flujo de Interacción con Outcome

**Última actualización:** 2026-01-31  
**Tipo:** Guía de aprendizaje - Patrón de diseño

---

## El Problema

En DDD, cuando validamos operaciones que involucran múltiples agregados, necesitamos:
1. Acumular errores de múltiples validadores
2. No lanzar excepción en el primer error (queremos ver TODOS los problemas)
3. Mantener el control de flujo sin try-catch anidados
4. Reportar todos los errores al usuario de una vez

**Caso concreto:** Desactivar un usuario requiere validar Dentist, Patient, Guardian, etc.

---

## La Solución: Patrón Outcome

### ¿Qué es Outcome?

`Outcome` es un contenedor que puede representar:
- **Éxito:** Operación completada sin problemas
- **Fallo:** Una o más violaciones de reglas de negocio

```java
public class Outcome<T> {
    private final boolean success;
    private final T value;
    private final List<OutcomeDetail> details;
    
    // Factory methods
    public static <T> Outcome<T> ok() { ... }
    public static <T> Outcome<T> ok(T value) { ... }
    public static <T> Outcome<T> fail(OutcomeDetail detail) { ... }
    
    // Métodos de combinación
    public Outcome<T> merge(Outcome<?> other) { ... }
    public Outcome<T> addDetail(OutcomeDetail detail) { ... }
    
    // Consultas
    public boolean isSuccess() { ... }
    public boolean isFailure() { ... }
    public List<OutcomeDetail> getDetails() { ... }
}
```

### OutcomeDetail: Representación de un Error

```java
public record OutcomeDetail(
    String code,           // del ErrorCatalog
    Severity severity,     // ERROR, WARNING, INFO
    Category category      // CLINICO, TECNICO, NEGOCIO
) {}
```

---

## Flujo Visual Completo

```
┌─────────────────────────────────────────────────────────────────┐
│  APPLICATION SERVICE (Capa de Aplicación)                       │
│                                                                 │
│  public void deactivateUser(UserId userId) {                   │
│      UserIdentity user = userRepo.findById(userId);            │
│                                                                 │
│      // 1. Invocar policy (orquestador)                        │
│      Outcome<Void> validation =                                │
│          userDeactivationPolicy.validate(user);                │
│                                                                 │
│      // 2. Verificar resultado global                          │
│      if (validation.isFailure()) {                             │
│          throw new AggregateBusinessRuleViolation(             │
│              validation.getDetails()  // TODOS los errores     │
│          );                                                     │
│      }                                                          │
│                                                                 │
│      // 3. Si todo OK, ejecutar acción                         │
│      user.deactivate();                                        │
│      userRepo.save(user);                                      │
│  }                                                              │
└─────────────────────────────────────────────────────────────────┘
                            ↓ invoca
┌─────────────────────────────────────────────────────────────────┐
│  USER DEACTIVATION POLICY (Orquestador)                        │
│                                                                 │
│  public Outcome<Void> validate(UserIdentity user) {            │
│      Outcome<Void> result = Outcome.ok();                      │
│                                                                 │
│      switch (user.getRole()) {                                 │
│          case DENTIST:                                         │
│              result = result.merge(                            │
│                  dentistValidator.validate(user.getId())       │
│              );                                                 │
│              break;                                             │
│                                                                 │
│          case PATIENT:                                         │
│              result = result.merge(                            │
│                  patientValidator.validate(user.getId())       │
│              );                                                 │
│              break;                                             │
│                                                                 │
│          case GUARDIAN:                                        │
│              Guardian guardian = guardianRepo                  │
│                  .findByUserId(user.getId());                  │
│              if (guardian != null) {                           │
│                  result = result.merge(                        │
│                      guardian.validateDeactivation()           │
│                  );                                             │
│              }                                                  │
│              break;                                             │
│      }                                                          │
│                                                                 │
│      return result;  // Outcome acumulado                      │
│  }                                                              │
└─────────────────────────────────────────────────────────────────┘
         ↓                    ↓                    ↓
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ DentistValidator │  │ PatientValidator │  │     Guardian     │
│                  │  │                  │  │  .validateDea... │
│ validate(id)     │  │ validate(id)     │  │                  │
│ → Outcome        │  │ → Outcome        │  │ → Outcome        │
└──────────────────┘  └──────────────────┘  └──────────────────┘
```

---

## Implementación Paso a Paso

### Paso 1: Validadores especializados devuelven Outcome

```java
// Domain Service especializado
@Service
public class DentistDeactivationValidator {
    
    private final ScheduleRepository scheduleRepo;
    
    public Outcome<Void> validate(DentistId dentistId) {
        Schedule schedule = scheduleRepo.findByDentistId(dentistId);
        
        // Validar si tiene citas pendientes
        if (schedule.hasAppointmentsWithinHours(24)) {
            return Outcome.fail(new OutcomeDetail(
                ErrorCatalog.ERR_DENTIST_PENDING_APPOINTMENTS,
                Severity.ERROR,
                Category.CLINICO
            ));
        }
        
        return Outcome.ok();
    }
}
```

```java
// Domain Service para pacientes
@Service
public class PatientDeactivationValidator {
    
    private final TreatmentRepository treatmentRepo;
    
    public Outcome<Void> validate(PatientId patientId) {
        List<Treatment> activeTreatments = 
            treatmentRepo.findActiveByPatientId(patientId);
        
        if (!activeTreatments.isEmpty()) {
            return Outcome.fail(new OutcomeDetail(
                ErrorCatalog.ERR_PATIENT_ACTIVE_TREATMENTS,
                Severity.ERROR,
                Category.CLINICO
            ));
        }
        
        return Outcome.ok();
    }
}
```

```java
// Validación en el propio agregado
public class Guardian {
    private List<PatientId> patientList;
    
    public Outcome<Void> validateDeactivation() {
        if (!patientList.isEmpty()) {
            return Outcome.fail(new OutcomeDetail(
                ErrorCatalog.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS,
                Severity.INFO,
                Category.CLINICO
            ));
        }
        
        return Outcome.ok();
    }
}
```

---

### Paso 2: Policy acumula Outcomes con merge()

```java
@Service
public class UserDeactivationPolicy {
    
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;
    private final GuardianRepository guardianRepo;
    
    public Outcome<Void> validate(UserIdentity user) {
        // Iniciar con éxito
        Outcome<Void> result = Outcome.ok();
        
        // Validar según rol y acumular errores
        switch (user.getRole()) {
            case DENTIST:
                Outcome<Void> dentistOutcome = 
                    dentistValidator.validate(user.getId());
                result = result.merge(dentistOutcome);
                break;
                
            case PATIENT:
                Outcome<Void> patientOutcome = 
                    patientValidator.validate(user.getId());
                result = result.merge(patientOutcome);
                break;
                
            case GUARDIAN:
                Guardian guardian = guardianRepo.findByUserId(user.getId());
                if (guardian != null) {
                    Outcome<Void> guardianOutcome = 
                        guardian.validateDeactivation();
                    result = result.merge(guardianOutcome);
                }
                break;
        }
        
        return result;  // Puede contener 0, 1 o múltiples errores
    }
}
```

**Cómo funciona `merge()`:**
```java
public Outcome<T> merge(Outcome<?> other) {
    if (other.isSuccess()) {
        return this;  // No agregar nada si el otro es éxito
    }
    
    // Agregar todos los detalles del otro outcome
    List<OutcomeDetail> merged = new ArrayList<>(this.details);
    merged.addAll(other.details);
    
    return new Outcome<>(false, null, merged);
}
```

---

### Paso 3: Application Service interpreta Outcome

```java
@Service
@Transactional
public class DeactivateUserUseCase {
    
    private final UserRepository userRepo;
    private final UserDeactivationPolicy deactivationPolicy;
    
    public void execute(UserId userId) {
        // 1. Obtener usuario
        UserIdentity user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        // 2. Validar con policy (acumula TODOS los errores)
        Outcome<Void> validation = deactivationPolicy.validate(user);
        
        // 3. Si hay errores, lanzar excepción con TODOS
        if (validation.isFailure()) {
            throw new AggregateBusinessRuleViolationException(
                validation.getDetails()
            );
        }
        
        // 4. Si todo OK, ejecutar acción
        user.deactivate();
        userRepo.save(user);
    }
}
```

---

## Escenarios de Uso

### Escenario 1: Usuario válido (sin errores)

```
User: Dentist sin citas, sin tratamientos

DentistValidator.validate()
└─→ Outcome.ok()

Policy.validate()
├─→ result = Outcome.ok()
├─→ merge(Outcome.ok())  // no agrega nada
└─→ return Outcome.ok()

Application Service
├─→ validation.isFailure() = false
└─→ user.deactivate() ✅
```

---

### Escenario 2: Usuario con UN error

```
User: Dentist con citas pendientes

DentistValidator.validate()
└─→ Outcome.fail(ERR_DENTIST_PENDING_APPOINTMENTS)

Policy.validate()
├─→ result = Outcome.ok()
├─→ merge(Outcome.fail(...))
└─→ return Outcome.fail([ERR_DENTIST_PENDING_APPOINTMENTS])

Application Service
├─→ validation.isFailure() = true
└─→ throw AggregateBusinessRuleViolationException([
      ERR_DENTIST_PENDING_APPOINTMENTS
    ])
```

---

### Escenario 3: Usuario con MÚLTIPLES errores

```
User: Paciente con tratamientos activos Y como guardián tiene pacientes asignados

PatientValidator.validate()
└─→ Outcome.fail(ERR_PATIENT_ACTIVE_TREATMENTS)

Guardian.validateDeactivation()
└─→ Outcome.fail(ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS)

Policy.validate()
├─→ result = Outcome.ok()
├─→ merge(Outcome.fail(ERR_PATIENT_ACTIVE_TREATMENTS))
│   └─→ result = Outcome.fail([ERR_PATIENT_ACTIVE_TREATMENTS])
├─→ merge(Outcome.fail(ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS))
│   └─→ result = Outcome.fail([
│         ERR_PATIENT_ACTIVE_TREATMENTS,
│         ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS
│       ])
└─→ return result

Application Service
├─→ validation.isFailure() = true
└─→ throw AggregateBusinessRuleViolationException([
      ERR_PATIENT_ACTIVE_TREATMENTS,
      ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS
    ])
```

**Respuesta HTTP (REST):**
```json
{
  "errors": [
    {
      "code": "ERR_PATIENT_ACTIVE_TREATMENTS",
      "message": "Patient has active treatments",
      "severity": "ERROR",
      "category": "CLINICO"
    },
    {
      "code": "ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS",
      "message": "Guardian has active patient authorizations",
      "severity": "INFO",
      "category": "CLINICO"
    }
  ]
}
```

---

## Ventajas del Patrón

### 1. Acumulación de Errores
```java
// ❌ SIN Outcome: solo ves el primer error
if (hasAppointments) throw new Exception("Has appointments");
if (hasTreatments) throw new Exception("Has treatments");  // nunca se ejecuta

// ✅ CON Outcome: ves TODOS los errores
Outcome result = Outcome.ok()
    .merge(validateAppointments())  // puede fallar
    .merge(validateTreatments());   // se ejecuta igual
```

### 2. Control de Flujo sin Excepciones
```java
// ❌ SIN Outcome: try-catch anidados
try {
    validateDentist();
    try {
        validatePatient();
    } catch (Exception e) { /* ... */ }
} catch (Exception e) { /* ... */ }

// ✅ CON Outcome: flujo lineal
Outcome result = Outcome.ok()
    .merge(dentistValidator.validate())
    .merge(patientValidator.validate());
```

### 3. Composición Clara
```java
// Validadores independientes
Outcome<Void> validateAppointments() { ... }
Outcome<Void> validateTreatments() { ... }
Outcome<Void> validateGuardians() { ... }

// Se combinan fácilmente
Outcome<Void> validateAll() {
    return Outcome.ok()
        .merge(validateAppointments())
        .merge(validateTreatments())
        .merge(validateGuardians());
}
```

---

## Patrón de Implementación Recomendado

### 1. Domain Services devuelven Outcome
```java
public interface DentistDeactivationValidator {
    Outcome<Void> validate(DentistId id);
}
```

### 2. Agregados pueden devolver Outcome para validaciones internas
```java
public class Guardian {
    public Outcome<Void> validateDeactivation() { ... }
}
```

### 3. Policy acumula con merge()
```java
public class UserDeactivationPolicy {
    public Outcome<Void> validate(UserIdentity user) {
        return Outcome.ok()
            .merge(validator1.validate())
            .merge(validator2.validate());
    }
}
```

### 4. Application Service traduce a Exception
```java
if (outcome.isFailure()) {
    throw new AggregateBusinessRuleViolationException(
        outcome.getDetails()
    );
}
```

---

## Relación con ADRs

- **ADR-008:** UserDeactivationPolicy (usa este patrón)
- **ADR-010:** Ubicación de validaciones (define dónde devolver Outcome)
- **ADR-019:** AggregateBusinessRuleViolationException (recibe lista de OutcomeDetails)
- **ADR-023:** UserAccessValidator (usa Outcome internamente, traduce a Exception)

---

## Cuándo NO Usar Outcome

**NO uses Outcome para:**
- Validaciones simples de un solo agregado
- Operaciones que NO requieren acumular errores
- Casos donde quieres fail-fast (detener en primer error)

**Ejemplo donde NO es necesario:**
```java
// Simple validación: lanzar excepción directamente
public void updateEmail(Email newEmail) {
    if (newEmail == null) {
        throw new IllegalArgumentException("Email cannot be null");
    }
    this.email = newEmail;
}
```

---

## Resumen en Una Frase

**Outcome permite acumular múltiples errores de validación sin lanzar excepciones, para reportarlos todos juntos al usuario.**