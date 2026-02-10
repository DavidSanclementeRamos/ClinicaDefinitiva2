# Guía: Validaciones Cruzadas entre Agregados

**Última actualización:** 2026-01-31  
**Tipo:** Patrón de diseño en DDD

---

## El Problema

En DDD, los agregados son fronteras de consistencia que encapsulan sus propias invariantes. Pero ¿qué pasa cuando una operación requiere validar reglas que involucran múltiples agregados?

**Ejemplo del dominio clínico:**

Antes de desactivar un `UserIdentity`, necesitamos verificar:
- El agregado `Dentist` asociado no tiene citas pendientes
- El agregado `Patient` asociado no tiene tratamientos activos
- El agregado `Guardian` asociado no tiene responsabilidades activas

### Tensión fundamental

```
Independencia de agregados    ⚔️    Reglas de negocio cruzadas
(cada uno autónomo)                  (coordinación necesaria)
```

Si `UserIdentity` valida directamente contra `Dentist`, rompe independencia.  
Si no valida, permite desactivaciones inconsistentes.

---

## El Patrón: Domain Service como Coordinador

### Regla de decisión

**¿Dónde ubicar la validación?**

| Condición | Ubicación |
|-----------|-----------|
| Validación depende **solo del estado interno** del agregado | Método en el agregado |
| Validación requiere **coordinar con otro agregado** | Domain Service especializado |
| Validación involucra **múltiples agregados** | Policy (orquestador de domain services) |

---

## Implementación del Patrón

### Nivel 1: Validación interna (en el agregado)

```java
// Caso simple: Guardian tiene lista interna de pacientes
public class Guardian {
    private List<PatientId> patientList;
    
    public Outcome<Void> validateDeactivation() {
        if (!patientList.isEmpty()) {
            return Outcome.fail(
                ErrorCatalog.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS
            );
        }
        return Outcome.ok();
    }
}
```

**Cuándo usar:** La regla depende solo de atributos del agregado.

---

### Nivel 2: Domain Service especializado

```java
// Caso complejo: Dentist requiere consultar Schedule
public class DentistDeactivationValidator {
    
    private final ScheduleRepository scheduleRepo;
    
    public Outcome<Void> validate(DentistId dentistId) {
        Schedule schedule = scheduleRepo.findByDentistId(dentistId);
        
        if (schedule.hasAppointmentsWithinHours(24)) {
            return Outcome.fail(
                ErrorCatalog.ERR_DENTIST_PENDING_APPOINTMENTS
            );
        }
        
        return Outcome.ok();
    }
}
```

**Cuándo usar:** La regla requiere coordinar con otro agregado o consultar repositorios.

---

### Nivel 3: Policy como orquestador

```java
// Caso complejo: validar TODOS los roles del usuario
public class UserDeactivationPolicy {
    
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;
    private final GuardianRepository guardianRepo;
    
    public Outcome<Void> validate(UserIdentity user) {
        Outcome<Void> result = Outcome.ok();
        
        // Validar según rol
        switch (user.getRole()) {
            case DENTIST:
                result = result.merge(
                    dentistValidator.validate(user.getId())
                );
                break;
                
            case PATIENT:
                result = result.merge(
                    patientValidator.validate(user.getId())
                );
                break;
                
            case GUARDIAN:
                Guardian guardian = guardianRepo.findByUserId(user.getId());
                if (guardian != null) {
                    result = result.merge(
                        guardian.validateDeactivation()
                    );
                }
                break;
        }
        
        return result;
    }
}
```

**Cuándo usar:** Múltiples validaciones que deben coordinarse, diferentes según contexto.

---

## Flujo Completo en el Sistema

```
┌─────────────────────────────────────────────────────────────┐
│  Application Service (Capa de Aplicación)                  │
│                                                             │
│  public void deactivateUser(UserId userIdentityId) {               │
│      UserIdentity user = userRepo.findById(userIdentityId);        │
│                                                             │
│      // 1. Validar acceso (técnico)                        │
│      userAccessValidator.validate(userIdentityId, now);            │
│      ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  │
│                                                             │
│      // 2. Validar restricciones de desactivación          │
│      Outcome validation = userDeactivationPolicy           │
│          .validate(user);                                  │
│      ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  │
│                                                             │
│      if (validation.isFailure()) {                         │
│          throw new AggregateBusinessRuleViolation(         │
│              validation.getDetails()                       │
│          );                                                 │
│      }                                                      │
│      ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  │
│                                                             │
│      // 3. Ejecutar acción en el agregado                  │
│      user.deactivate();                                    │
│      userRepo.save(user);                                  │
│  }                                                          │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  UserDeactivationPolicy (Domain Service Orquestador)       │
│                                                             │
│  - Coordina múltiples validaciones                         │
│  - Delega en validadores especializados                    │
│  - Acumula resultados en Outcome                           │
└─────────────────────────────────────────────────────────────┘
                          │
            ┌─────────────┼─────────────┐
            ↓             ↓             ↓
   ┌────────────┐  ┌────────────┐  ┌────────────┐
   │  Dentist   │  │  Patient   │  │  Guardian  │
   │ Validator  │  │ Validator  │  │ .validate()│
   └────────────┘  └────────────┘  └────────────┘
```

---

## Anti-Patrones a Evitar

### ❌ Anti-Patrón 1: Agregado conoce demasiado

```java
// MAL: UserIdentity consulta directamente a otros agregados
public class UserIdentity {
    
    public void deactivate(
        DentistRepository dentistRepo,
        PatientRepository patientRepo
    ) {
        // ¡Violación! Agregado no debe conocer repositorios externos
        Dentist dentist = dentistRepo.findByUserId(this.id);
        if (dentist.hasPendingAppointments()) {
            throw new Exception("Cannot deactivate");
        }
        
        this.status = INACTIVE;
    }
}
```

**Por qué es malo:**
- Agregado acoplado a infraestructura (repositorios)
- Rompe límites de agregado
- Difícil de testear

---

### ❌ Anti-Patrón 2: Validación duplicada

```java
// MAL: Cada agregado duplica la validación
public class Patient {
    public void register(UserIdentity user) {
        if (!user.isActive()) throw... // ← Duplicado
    }
}

public class Dentist {
    public void register(UserIdentity user) {
        if (!user.isActive()) throw... // ← Duplicado
    }
}
```

**Por qué es malo:**
- Código duplicado
- Difícil mantener consistencia
- Cambios requieren tocar múltiples agregados

---

### ❌ Anti-Patrón 3: Validación en Application Service

```java
// MAL: Lógica de negocio en capa de aplicación
public class DeactivateUserUseCase {
    
    public void execute(UserId userIdentityId) {
        // Validaciones directas en el use case
        Dentist dentist = dentistRepo.findByUserId(userIdentityId);
        if (dentist != null && dentist.hasPendingAppointments()) {
            throw new Exception("Dentist has pending appointments");
        }
        
        Patient patient = patientRepo.findByUserId(userIdentityId);
        if (patient != null && patient.hasActiveTreatments()) {
            throw new Exception("Patient has active treatments");
        }
        
        // ... más validaciones ...
        
        user.deactivate();
    }
}
```

**Por qué es malo:**
- Lógica de negocio fuera del dominio
- Difícil de testear sin infraestructura
- No reutilizable

---

## Patrón Correcto: Separación en Capas

```java
// ✅ CORRECTO

// 1. Agregado: solo validaciones internas
public class Guardian {
    public Outcome<Void> validateDeactivation() {
        if (!patientList.isEmpty()) {
            return Outcome.fail(...);
        }
        return Outcome.ok();
    }
}

// 2. Domain Service: validaciones cruzadas
public class DentistDeactivationValidator {
    public Outcome<Void> validate(DentistId id) {
        Schedule schedule = scheduleRepo.findByDentistId(id);
        return schedule.hasAppointmentsWithinHours(24)
            ? Outcome.fail(...)
            : Outcome.ok();
    }
}

// 3. Policy: orquestador
public class UserDeactivationPolicy {
    public Outcome<Void> validate(UserIdentity user) {
        // Coordina validadores según rol
        return switch (user.getRole()) {
            case DENTIST -> dentistValidator.validate(user.getId());
            case GUARDIAN -> guardianRepo.findByUserId(user.getId())
                .validateDeactivation();
            // ...
        };
    }
}

// 4. Application Service: coordina todo
@Transactional
public void deactivateUser(UserId userIdentityId) {
    UserIdentity user = userRepo.findById(userIdentityId);
    
    userAccessValidator.validate(userIdentityId, now);
    
    Outcome validation = userDeactivationPolicy.validate(user);
    if (validation.isFailure()) {
        throw new AggregateBusinessRuleViolationException(
            validation.getDetails()
        );
    }
    
    user.deactivate();
    userRepo.save(user);
}
```

---

## Checklist de Decisión

Cuando te enfrentes a una validación cruzada, pregúntate:

- [ ] ¿La validación depende solo del estado interno del agregado?
    - **SÍ** → Método en el agregado

- [ ] ¿La validación requiere consultar otro agregado o repositorio?
    - **SÍ** → Domain Service especializado

- [ ] ¿La validación involucra múltiples agregados con lógica condicional?
    - **SÍ** → Policy como orquestador

- [ ] ¿La validación solo coordina, sin lógica de negocio propia?
    - **SÍ** → Application Service

---

## ADRs que Aplican Este Patrón

- **ADR-008:** UserDeactivationPolicy como orquestador
- **ADR-010:** Ubicación de validaciones de desactivación
- **ADR-023:** UserAccessValidator como domain service

---

## Referencias

- Evans, Eric. *Domain-Driven Design*. Capítulo sobre Agregados
- Vernon, Vaughn. *Implementing Domain-Driven Design*. Capítulo sobre Domain Services
- ADR-008: Implementación concreta de este patrón