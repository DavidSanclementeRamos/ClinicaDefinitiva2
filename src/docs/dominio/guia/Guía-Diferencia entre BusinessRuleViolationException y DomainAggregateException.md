# Guía: Diferencia entre BusinessRuleViolationException y DomainAggregateException

**Última actualización:** 2026-01-31  
**Tipo:** Guía de aprendizaje

---

## El Problema

Tras la simplificación de la jerarquía de excepciones (ADR-018), quedaron dos tipos de excepciones en el dominio:
- `BusinessRuleViolationException`
- `DomainAggregateException`

Ambas heredan de `ModelException` y usan el mismo `ErrorCatalog`, pero tienen **semánticas diferentes**.

**Pregunta frecuente:** ¿Cuándo uso cada una?

---

## Diferencia Semántica

### BusinessRuleViolationException

**Representa:** Violación de una **regla de negocio específica**

**Cuándo usarla:**
- Una operación incumple una restricción explícita del dominio
- La regla es puntual y verificable
- El agregado está bien formado, pero la acción no está permitida

**Ejemplos:**
```java
// Regla: Dentista debe ser mayor de 18 años
if (age < 18) {
    throw new BusinessRuleViolationException(
        ErrorCatalog.ERR_DENTIST_MINIMUM_AGE,
        Severity.ERROR,
        Category.CLINICO
    );
}

// Regla: No puede haber citas solapadas
if (schedule.hasOverlappingAppointment(newAppointment)) {
    throw new BusinessRuleViolationException(
        ErrorCatalog.ERR_APPOINTMENT_OVERLAP,
        Severity.ERROR,
        Category.CLINICO
    );
}

// Regla: El dentista debe tener disponibilidad mínima
if (!dentist.meetsMinimumAvailability()) {
    throw new BusinessRuleViolationException(
        ErrorCatalog.ERR_DENTIST_INSUFFICIENT_AVAILABILITY,
        Severity.WARNING,
        Category.CLINICO
    );
}
```

**Características:**
- **Nivel:** Regla puntual dentro de un agregado
- **Naturaleza:** Restricción de negocio
- **Granularidad:** Fina (regla específica)
- **Momento:** Durante una operación (update, create, delete)

---

### DomainAggregateException

**Representa:** Condición **inválida o inconsistente en el estado global** de un agregado

**Cuándo usarla:**
- El agregado en su conjunto no cumple con las invariantes necesarias
- Falta información estructural obligatoria
- El agregado no puede existir/operar en su estado actual

**Ejemplos:**
```java
// Invariante: Patient menor de edad REQUIERE Guardian asignado
if (patient.isMinor() && patient.getGuardianId() == null) {
    throw new DomainAggregateException(
        ErrorCatalog.ERR_PATIENT_REQUIRES_GUARDIAN,
        Severity.ERROR,
        Category.CLINICO
    );
}

// Invariante: Appointment REQUIERE fechas válidas
if (appointment.getStartTime() == null || appointment.getEndTime() == null) {
    throw new DomainAggregateException(
        ErrorCatalog.ERR_APPOINTMENT_INVALID_DATES,
        Severity.ERROR,
        Category.CLINICO
    );
}

// Invariante: Treatment REQUIERE al menos una fase
if (treatment.getPhases().isEmpty()) {
    throw new DomainAggregateException(
        ErrorCatalog.ERR_TREATMENT_NO_PHASES,
        Severity.ERROR,
        Category.CLINICO
    );
}
```

**Características:**
- **Nivel:** Estado global del agregado
- **Naturaleza:** Inconsistencia estructural o falta de invariante
- **Granularidad:** Gruesa (agregado completo)
- **Momento:** Durante construcción o validación estructural

---

## Tabla Comparativa

| Aspecto | BusinessRuleViolationException | DomainAggregateException |
|---------|-------------------------------|--------------------------|
| **¿Qué valida?** | Regla de negocio puntual | Invariante estructural del agregado |
| **Cuándo ocurre?** | Durante una operación | Durante construcción o validación global |
| **El agregado está...** | Bien formado, pero la acción no permitida | Mal formado o incompleto |
| **Granularidad** | Fina (una regla) | Gruesa (agregado completo) |
| **Ejemplo típico** | "Edad mínima no cumplida" | "Paciente sin guardián requerido" |
| **Se puede recuperar?** | A veces (ajustar datos y reintentar) | Difícilmente (falta info estructural) |

---

## Casos de Uso Completos

### Caso 1: Registrar Dentist

```java
public static Dentist create(
    PersonalInfo personalInfo,
    LicenseNumber license,
    Specialization specialization,
    WorkingHours workingHours
) {
    // Validación de invariantes (DomainAggregateException)
    if (personalInfo == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_DENTIST_REQUIRES_PERSONAL_INFO,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    if (license == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_DENTIST_REQUIRES_LICENSE,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    // Validación de reglas de negocio (BusinessRuleViolationException)
    if (personalInfo.getAge() < 18) {
        throw new BusinessRuleViolationException(
            ErrorCatalog.ERR_DENTIST_MINIMUM_AGE,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    if (workingHours.getDeclaredHoursPerWeek() < 20) {
        throw new BusinessRuleViolationException(
            ErrorCatalog.ERR_DENTIST_INSUFFICIENT_WORKING_HOURS,
            Severity.WARNING,
            Category.CLINICO
        );
    }
    
    return new Dentist(personalInfo, license, specialization, workingHours);
}
```

**Explicación:**
- `personalInfo == null` → **DomainAggregateException** (falta estructura básica)
- `age < 18` → **BusinessRuleViolationException** (regla de negocio específica)

---

### Caso 2: Actualizar Schedule

```java
public void updateAvailability(WeeklyAvailability newAvailability) {
    // Validación de invariante
    if (newAvailability == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_SCHEDULE_REQUIRES_AVAILABILITY,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    // Validación de regla de negocio
    if (newAvailability.totalHours() < this.workingHours.getDeclaredHoursPerWeek()) {
        throw new BusinessRuleViolationException(
            ErrorCatalog.ERR_SCHEDULE_BELOW_WORKING_HOURS,
            Severity.WARNING,
            Category.CLINICO
        );
    }
    
    // Validación de regla de negocio (conflicto con citas existentes)
    if (hasConflictWithExistingAppointments(newAvailability)) {
        throw new BusinessRuleViolationException(
            ErrorCatalog.ERR_SCHEDULE_CONFLICTS_WITH_APPOINTMENTS,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    this.availability = newAvailability;
}
```

**Explicación:**
- `newAvailability == null` → **DomainAggregateException** (estructura inválida)
- `totalHours() < declaredHours` → **BusinessRuleViolationException** (regla de cumplimiento)
- `hasConflictWithAppointments` → **BusinessRuleViolationException** (regla de solapamiento)

---

### Caso 3: Crear Patient

```java
public static Patient create(
    PersonalInfo personalInfo,
    UserId userIdentityId,
    GuardianId guardianId  // puede ser null si es mayor de edad
) {
    // Validación de invariantes
    if (personalInfo == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_PATIENT_REQUIRES_PERSONAL_INFO,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    if (userIdentityId == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_PATIENT_REQUIRES_USER,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    // Validación de regla de negocio
    boolean isMinor = personalInfo.getAge() < 18;
    if (isMinor && guardianId == null) {
        throw new DomainAggregateException(
            ErrorCatalog.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN,
            Severity.ERROR,
            Category.CLINICO
        );
    }
    
    return new Patient(personalInfo, userIdentityId, guardianId);
}
```

**Nota:** `isMinor && guardianId == null` es **DomainAggregateException** porque es una invariante estructural: un paciente menor NO PUEDE existir sin guardián. No es una regla de negocio puntual, es parte de la definición del agregado.

---

## Regla Práctica de Decisión

### Usa BusinessRuleViolationException si:
- [ ] El agregado está bien formado
- [ ] La operación específica no está permitida
- [ ] Puedes describir la regla en una frase simple
- [ ] La regla se puede verificar en tiempo de ejecución

**Ejemplo:** "No puede agendar cita si el dentista está de vacaciones"

### Usa DomainAggregateException si:
- [ ] Falta información estructural obligatoria
- [ ] El agregado no puede existir en ese estado
- [ ] Es una invariante que define al agregado
- [ ] El problema es de construcción, no de operación

**Ejemplo:** "Paciente menor de edad sin guardián asignado"

---

## Anti-Patrones a Evitar

### ❌ Anti-Patrón 1: Usar DomainAggregateException para reglas de negocio

```java
// MAL: Esta es una regla de negocio, no una invariante estructural
if (dentist.getYearsOfExperience() < 2) {
    throw new DomainAggregateException(
        ErrorCatalog.ERR_DENTIST_INEXPERIENCED,
        Severity.WARNING,
        Category.CLINICO
    );
}

// BIEN: Usar BusinessRuleViolationException
if (dentist.getYearsOfExperience() < 2) {
    throw new BusinessRuleViolationException(
        ErrorCatalog.ERR_DENTIST_MINIMUM_EXPERIENCE,
        Severity.WARNING,
        Category.CLINICO
    );
}
```

---

### ❌ Anti-Patrón 2: Usar BusinessRuleViolationException para invariantes

```java
// MAL: Esta es una invariante estructural, no una regla puntual
if (appointment.getStartTime() == null) {
    throw new BusinessRuleViolationException(
        ErrorCatalog.ERR_APPOINTMENT_NO_START_TIME,
        Severity.ERROR,
        Category.CLINICO
    );
}

// BIEN: Usar DomainAggregateException
if (appointment.getStartTime() == null) {
    throw new DomainAggregateException(
        ErrorCatalog.ERR_APPOINTMENT_INVALID_DATES,
        Severity.ERROR,
        Category.CLINICO
    );
}
```

---

### ❌ Anti-Patrón 3: Confundir null check con regla de negocio

```java
// Pregunta: ¿Qué excepción usar?
if (patient.getGuardianId() == null) {
    throw ???
}
```

**Respuesta:** Depende del contexto:

```java
// Si es durante CONSTRUCCIÓN de paciente menor:
// → DomainAggregateException (invariante)
if (isMinor && guardianId == null) {
    throw new DomainAggregateException(...);
}

// Si es durante ACTUALIZACIÓN de un campo diferente:
// → Puede ser OK que sea null (paciente mayor)
// → No lanzar excepción

// Si es durante VALIDACIÓN de operación sensible:
// → BusinessRuleViolationException (regla de negocio)
if (operation.requiresGuardian() && patient.getGuardianId() == null) {
    throw new BusinessRuleViolationException(...);
}
```

---

## Relación con ADRs

- **ADR-018:** Simplificación general de jerarquía de excepciones (introduce estas dos)
- **ADR-019:** AggregateBusinessRuleViolationException (para múltiples violaciones)

---

## Checklist de Decisión

Cuando vayas a lanzar una excepción, pregúntate:

1. **¿Falta información obligatoria para que el agregado exista?**
    - SÍ → `DomainAggregateException`

2. **¿Es una regla específica que valida una operación?**
    - SÍ → `BusinessRuleViolationException`

3. **¿El problema es de construcción o de operación?**
    - Construcción → `DomainAggregateException`
    - Operación → `BusinessRuleViolationException`

4. **¿El agregado puede existir sin cumplir esto?**
    - NO puede existir → `DomainAggregateException`
    - SÍ puede existir, pero no operar → `BusinessRuleViolationException`

---

## Resumen en Una Frase

```
DomainAggregateException = "Este agregado NO PUEDE existir así"
BusinessRuleViolationException = "Esta OPERACIÓN no está permitida"
```