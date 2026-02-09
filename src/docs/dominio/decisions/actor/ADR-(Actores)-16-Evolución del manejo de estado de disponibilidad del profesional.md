# ADR-16 (Actores): Evolución del manejo de estado de disponibilidad del profesional

- **Estado:** Aprobado
- **Fecha:** 2026-02-08
- **Autor:** David
- **Categoría:** Dominio - Lecciones Aprendidas
- **Relacionado con:** ADR-09 (Cita - Validaciones redundantes)

---

## Resumen Ejecutivo

Este ADR documenta la **evolución del diseño del estado de disponibilidad del dentista** y la corrección de múltiples errores arquitectónicos que se acumularon durante el desarrollo temprano del módulo de Actor.

**Contexto temporal crítico:** El módulo de Actor fue **uno de los primeros en desarrollarse** durante la migración, en un momento donde:
-  No existía el módulo de Schedule (citas) consolidado
-  No había separación clara entre autenticación y autorización
-  No se comprendía completamente el rol de `Shift` como fuente de verdad temporal
-  Se mezclaban responsabilidades entre agregados sin criterio claro

En ese contexto de incertidumbre arquitectónica, **parecía razonable** implementar validaciones de vacaciones dentro del agregado `Dentist`, usar muchos estados en el VO `DentistAvailabilityStatus`, y delegar validaciones de autorización a `UserStatus`. Solo después de consolidar los módulos de autenticación (**ADR-(User)-02**), autorización (**ADR-38**), y agendamiento (**ADR-09**), se hizo evidente que el diseño inicial era **fundamentalmente incorrecto**.

**Decisiones clave tras la consolidación:**

1. **Reducir estados** de `DentistAvailabilityStatus` de 10 a solo 3: `AVAILABLE`, `SICK_LEAVE`, `VACATION`
2. **Migrar validaciones** de vacaciones del agregado a un Domain Service
3. **Diferenciar semánticamente** ausencias planificadas (vacaciones) de imprevistas (incapacidad)
4. **Eliminar acoplamiento** entre `Dentist` y `UserIdentity`/`Appointment`
5. **Coordinar efectos secundarios** (cancelación de citas) desde Domain Services

---

## Contexto: La Evolución del Problema

**IMPORTANTE:** Las decisiones que aquí se documentan como "errores" fueron tomadas en las **primeras semanas del proyecto**, cuando el módulo de Actor era uno de los pocos existentes y no había arquitectura madura de referencia. En aquel momento, **estas decisiones parecían razonables** dada la información disponible. Solo la consolidación posterior de otros módulos reveló las limitaciones del diseño inicial.

### Fase 1: Primeros Intentos (Módulo Inmaduro) - **SIN módulos consolidados**

**Estado de la arquitectura en este punto:**
-  `Dentist` agregado creado
-  No existía `Shift` como fuente de verdad temporal
-  No existía `Appointment` consolidado
-  No había `UserIdentity` como agregado rico
-  No existía módulo de autorización
-  Se intentaba cubrir "todos los escenarios posibles" en un solo VO

**En ese contexto de incertidumbre, las decisiones iniciales fueron:**

#### Error 1: Validación de Vacaciones Dentro del Agregado `Dentist`

> "Si el dentista valida sus propias vacaciones, estaremos seguros de que no puede tomar vacaciones cuando tenga citas programadas."

**Esta lógica parecía sólida en su momento** porque:
1. No había un `ScheduleQueryService` para consultar citas
2. No se comprendía el concepto de Domain Services para coordinación
3. Parecía "cohesivo" que el dentista validara su propia disponibilidad
4. Los ejemplos encontrados mezclaban estas responsabilidades

**Código inicial (problemático):**

```java
public class Dentist {
    private DentistId id;
    private UserIdentity user;  // ❌ Acoplamiento 1
    private DentistAvailabilityStatus availabilityStatus;
    
    /**
     * ❌ PROBLEMA: Validación mezclada con múltiples agregados
     */
    public void validateVacationRequest(
            UserIdentity user,           // ❌ Parámetro redundante
            LocalDateTime vacationStart,
            LocalDateTime vacationEnd,
            Schedule schedule) {         // ❌ Acoplamiento 2
        
        // ❌ PROBLEMA 1: Validación de autorización en lugar incorrecto
        UserStatus.from(user).mustBeActive(
            ErrorCatalogXD.ERR_DENTIST_NOT_EDITABLE,
            EntityContext.DENTIST
        );

        // ✅ Validación básica correcta
        if (!TimeIntervalRules.isValid(vacationStart, vacationEnd)) {
            throw new BusinessRuleViolationException(
                DentistError.ERR_DENTIST_INVALID_VACATION_RANGE,
                EntityContext.DENTIST
            );
        }

        // ❌ PROBLEMA 2: Dentist conoce estructura interna de Schedule
        List<Appointment> conflicts = schedule.getAppointments().stream()
                .filter(a -> TimeIntervalRules.overlaps(
                    a.getStart(), a.getEnd(),
                    vacationStart, vacationEnd
                ))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                DentistError.ERR_DENTIST_VACATION_CONFLICT,
                EntityContext.DENTIST
            );
        }
        
        // ❌ PROBLEMA 3: No cambia el estado, validación incompleta
        // this.availabilityStatus = ... (faltaba)
    }
}
```

**Problemas identificados retrospectivamente:**

1. **Acoplamiento brutal:**
    - `Dentist` conoce `UserIdentity` (módulo de autenticación)
    - `Dentist` conoce `Schedule` y `Appointment` (módulo de agendamiento)
    - Viola límites de agregado y bounded contexts

2. **Validación de autorización en lugar incorrecto:**
    - `UserStatus.from(user).mustBeActive()` es responsabilidad de autenticación
    - Similar al error documentado en ADR-09 (Cita)

3. **No aplica el cambio de estado:**
    - Solo valida pero no muta `availabilityStatus`
    - Validación sin efecto

---

#### Error 2: Cambio de Estado Sin Coordinación

**Código inicial:**

```java
public class Dentist {
    
    /**
     * ❌ PROBLEMA: Permite cambiar estado sin validar efectos
     */
    public void changeAvailability(DentistAvailabilityStatus newStatus) {
        // Solo valida transición válida en la máquina de estados
        if (!this.availabilityStatus.canTransitionTo(newStatus.getCurrent())) {
            throw new BusinessRuleViolationException(
                VoActorError.ERR_AVAILABILITY_STATUS_INVALID_TRANSITION,
                EntityContext.DENTIST
            );
        }
        
        // ❌ PROBLEMA: Cambia estado sin validar:
        //    - ¿Tiene citas pendientes?
        //    - ¿Debe cancelar/reprogramar?
        //    - ¿Debe notificar pacientes?
        this.availabilityStatus = newStatus;
    }
}
```

**Problemas:**
- Permite transiciones "válidas" según el VO pero sin coordinar efectos secundarios
- Ejemplo: cambiar a `SICK_LEAVE` pero dejar citas activas
- Falta orquestación de cancelaciones/notificaciones

---

#### Error 3: Value Object con Demasiados Estados

**Código inicial de `DentistAvailabilityStatus`:**

```java
public class DentistAvailabilityStatus {
    
    public enum Status {
        AVAILABLE,          // ✅ Necesario
        UNAVAILABLE,        // ❌ Redundante con OFF_SHIFT
        ON_BREAK,           // ❌ Shift ya lo cubre
        IN_CONSULTATION,    // ❌ Appointment ya lo cubre
        OFF_SHIFT,          // ❌ Shift ya lo cubre
        ON_CALL,            // ❌ No aplica a odontólogos
        SICK_LEAVE,         // ✅ Necesario
        VACATION,           // ✅ Necesario
        TRAINING,           // ❌ Se puede modelar como ausencia
        ADMIN_TASK          // ❌ Redundante con OFF_SHIFT
    }
    
    private Status current;
    
    private static final EnumMap<Status, Set<Status>> validTransitions = ...;
    
    // ❌ PROBLEMA: VO mutable
    public boolean tryTransitionTo(Status next) {
        if (canTransitionTo(next)) {
            this.current = next;  // ❌ Mutación
            return true;
        }
        return false;
    }
    
    // ❌ PROBLEMA: Números mágicos para prioridad
    public int getPriorityLevel() {
        return switch (current) {
            case AVAILABLE       -> 3;
            case ON_CALL         -> 2;
            case TRAINING,
                 ADMIN_TASK      -> 1;
            case ON_BREAK,
                 IN_CONSULTATION -> 0;
            default              -> -1; // ❌ Número mágico
        };
    }
}
```

**Problemas identificados:**

1. **Demasiados estados:**
    - `IN_CONSULTATION` → Ya cubierto por `Appointment` activo
    - `ON_BREAK` → Ya cubierto por `Shift.excludedBlocks`
    - `OFF_SHIFT` → Ya cubierto por ausencia de `Shift` activo
    - `TRAINING`, `ADMIN_TASK` → Se pueden modelar como ausencias genéricas

2. **VO mutable:**
    - `tryTransitionTo()` muta `this.current`
    - Viola principio de inmutabilidad de Value Objects

3. **Números mágicos:**
    - Prioridad con `int` en lugar de `enum Priority`

---

### Fase 2: Consolidación de Módulos Relacionados - **Mejora Parcial**

**Cambio de paradigma:** Durante las siguientes semanas se consolidaron:

 **ADR-(User)-02:** `UserIdentity` como agregado rico
 **ADR-47:** Módulo de autorización (RBAC/ABAC)
 **ADR-09:** Eliminación de validaciones redundantes en agendamiento
 **ADR-(Cita)-07:** `Shift` como única fuente de verdad temporal

**Este fue el primer indicio de que la arquitectura inicial estaba mal**, porque ahora teníamos:
- Validación de estado en `UserIdentity.canPerformSensitiveAction()` ← **Correcto**
- Validación de estado en `Dentist.validateVacationRequest()` ← **Redundante pero aún no detectado**
- Estados operativos en `DentistAvailabilityStatus` ← **Redundantes con `Shift` y `Appointment`**

**En este punto, reconocí parte del problema:**

> "La validación de vacaciones está en el lugar incorrecto. Debería ser un Domain Service."

**Pero la dejé comentada** porque:
1. Estaba trabajando en el módulo de autenticación/autorización
2. Requería primero resolver el problema de validaciones redundantes en agendamiento (ADR-09)
3. No tenía aún el criterio completo para ver todos los errores

**Código comentado (esperando refactorización):**

```java
public class Dentist {
    
    // ⚠️ COMENTADO: Esperar a consolidar módulo Schedule
    /*
    public void validateVacationRequest(...) {
        // Este método debe migrar a un Domain Service
        // pero primero debo resolver problemas de acoplamiento
        // en el módulo de citas
    }
    */
}
```

---

### Fase 3: Momento de la Epifanía - **Tras consolidar Schedule (ADR-09)**

**Contexto del descubrimiento:**

Después de resolver los problemas de validaciones redundantes en el módulo de citas (**ADR-09**), volví a revisar el módulo de Actor para aplicar las mismas lecciones aprendidas.

**Al descoment el código de `validateVacationRequest()` para migrarlo a un Domain Service**, me di cuenta de **múltiples problemas que antes no había visto**:

```
ARQUITECTURA CONSOLIDADA:
┌─────────────────────────────────────────┐
│ 1. AUTENTICACIÓN (Login)                │
│    - Valida estado de usuario           │  ← UserIdentity.canPerformSensitiveAction()
│    - Carga roles                        │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ 2. AUTORIZACIÓN (Por operación)        │
│    - Valida permisos según rol          │  ← AuthorizationService.isAuthorized()
│    - Políticas RBAC/ABAC                │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ 3. OPERACIÓN DE NEGOCIO                 │
│    - Shift cubre tiempo operativo       │  ← Shift.canAccommodateAppointment()
│    - Appointment cubre ocupación        │
│    - DentistAvailabilityStatus cubre... │  ← ❓ ¿QUÉ CUBRE REALMENTE?
└─────────────────────────────────────────┘
```

**La epifanía llegó al preguntarme:**

> "Si `Shift` ya cubre cuándo el dentista está operativo, y `Appointment` ya cubre cuándo está ocupado, ¿para qué sirven estados como `IN_CONSULTATION`, `ON_BREAK`, `OFF_SHIFT` en el VO?"

**Respuesta:** No sirven para nada. Son **redundantes**.

---

## Problema: Los Tres Errores Fundamentales

Una vez consolidados los módulos relacionados, se hicieron evidentes **tres errores arquitectónicos**:

### Error 1: Acoplamiento en Validación de Vacaciones

**Observación crítica:**
> "`Dentist` no debe conocer `Schedule` ni `Appointment`. Eso es responsabilidad de un Domain Service."

**Violaciones detectadas:**

```java
// ❌ INCORRECTO: Dentist conoce otros agregados
public void validateVacationRequest(
        UserIdentity user,       // ❌ Acoplamiento con autenticación
        LocalDateTime start,
        LocalDateTime end,
        Schedule schedule) {     // ❌ Acoplamiento con agendamiento
    
    UserStatus.from(user).mustBeActive(...);  // ❌ Autorización en lugar incorrecto
    
    List<Appointment> conflicts = schedule.getAppointments()...  // ❌ Conoce internals
}
```

**Problemas:**
1. **Violación de límites de agregado:** `Dentist` depende de `UserIdentity` y `Schedule`
2. **Validación de autorización en lugar incorrecto:** Similar a ADR-09
3. **No aplica el cambio de estado:** Solo valida pero no muta

---

### Error 2: Cambio de Estado Sin Coordinación de Efectos

**Observación crítica:**
> "Cambiar a `SICK_LEAVE` debe cancelar/reprogramar citas automáticamente, no solo cambiar un flag."

**Código problemático:**

```java
// ❌ INCORRECTO: Cambia estado pero no coordina efectos
public void changeAvailability(DentistAvailabilityStatus newStatus) {
    if (!this.availabilityStatus.canTransitionTo(newStatus.getCurrent())) {
        throw new BusinessRuleViolationException(...);
    }
    
    // ❌ PROBLEMA: ¿Qué pasa con las citas?
    this.availabilityStatus = newStatus;
}
```

**Ejemplo del problema:**

```
Escenario:
- Dr. García tiene 3 citas programadas para mañana
- Hoy sufre un accidente y queda incapacitado
- Código actual: changeAvailability(SICK_LEAVE)

¿Qué pasa?
❌ Estado cambia a SICK_LEAVE
❌ Las 3 citas siguen activas (no se cancelan)
❌ Los pacientes no reciben notificación
❌ El sistema sigue permitiendo agendar más citas
```

**Falta orquestación de:**
- Cancelación de citas existentes
- Notificación a pacientes
- Publicación de eventos de dominio
- Invalidación de disponibilidad en caché

---

### Error 3: Estados Redundantes con `Shift` y `Appointment`

**Observación crítica tras ADR-(Cita)-07:**
> "Si `Shift` es la única fuente de verdad temporal, entonces `IN_CONSULTATION`, `ON_BREAK`, `OFF_SHIFT` son redundantes."

**Análisis estado por estado:**

| Estado | ¿Necesario? | Razón |
|--------|-------------|-------|
| `AVAILABLE` | ✅ Sí | Representa disponibilidad clínica mínima |
| `IN_CONSULTATION` | ❌ No | Ya cubierto por `Appointment` activo |
| `ON_BREAK` | ❌ No | Ya cubierto por `Shift.excludedBlocks` (almuerzo, etc.) |
| `OFF_SHIFT` | ❌ No | Ya cubierto por ausencia de `Shift` activo |
| `SICK_LEAVE` | ✅ Sí | Ausencia médica imprevista |
| `VACATION` | ✅ Sí | Ausencia planificada |
| `ON_CALL` | ❌ No | No aplica a odontólogos (concepto de urgencias) |
| `TRAINING` | ❌ No | Se puede modelar como ausencia genérica |
| `ADMIN_TASK` | ❌ No | Redundante con `OFF_SHIFT` o `Shift.excludedBlocks` |
| `UNAVAILABLE` | ❌ No | Redundante con `OFF_SHIFT` o ausencias |

**Conclusión:** Solo 3 estados son realmente necesarios:
- `AVAILABLE` → Disponible para atender
- `SICK_LEAVE` → Ausencia médica imprevista
- `VACATION` → Ausencia planificada

**Adicionalmente:**
- El VO era **mutable** (`tryTransitionTo()` modificaba `this.current`)
- Usaba **números mágicos** para prioridad en lugar de `enum Priority`

---

## Decisión

### 1. Reducir Estados del VO a lo Esencial

**Eliminar todos los estados redundantes:**

```java
// ❌ ANTES: 10 estados mezclando conceptos
public enum Status {
    AVAILABLE, UNAVAILABLE, ON_BREAK, IN_CONSULTATION,
    OFF_SHIFT, ON_CALL, SICK_LEAVE, VACATION, TRAINING, ADMIN_TASK
}

// ✅ DESPUÉS: 3 estados esenciales
public enum Status {
    AVAILABLE,      // Disponible para atender
    SICK_LEAVE,     // Ausencia médica imprevista
    VACATION        // Ausencia planificada
}
```

**Razón:**
- `Shift` cubre tiempo operativo (cuándo está en la clínica)
- `Appointment` cubre ocupación (cuándo está atendiendo)
- `DentistAvailabilityStatus` solo cubre **ausencias clínicas**

---

### 2. Hacer el VO Inmutable

**Eliminar mutación:**

```java
// ❌ ANTES: Mutable
public boolean tryTransitionTo(Status next) {
    if (canTransitionTo(next)) {
        this.current = next;  // ❌ Mutación
        return true;
    }
    return false;
}

// ✅ DESPUÉS: Inmutable (si fuera necesario transiciones)
public DentistAvailabilityStatus transitionTo(Status next) {
    if (!canTransitionTo(next)) {
        throw new IllegalStateException("Invalid transition");
    }
    return new DentistAvailabilityStatus(next);  // ✅ Nueva instancia
}
```

**Pero con solo 3 estados, las transiciones son simples:**
- No se necesita máquina de estados compleja
- El cambio de estado se maneja desde Domain Services

---

### 3. Migrar Validaciones a Domain Services

**Separar por tipo de ausencia:**

#### A. Vacaciones (Planificadas) → Requiere validación previa

```java
@Service
public class DentistVacationService {
    
    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    
    /**
     * ✅ CORRECTO: Validación en Domain Service
     */
    @Transactional
    public void requestVacation(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end) {
        
        // 1. Validar rango
        if (!TimeIntervalRules.isValid(start, end)) {
            throw new BusinessRuleViolationException(
                DentistError.ERR_DENTIST_INVALID_VACATION_RANGE,
                EntityContext.DENTIST
            );
        }
        
        // 2. Consultar citas (sin acoplar Dentist)
        List<Appointment> conflicts = appointmentRepository
            .findByDentistBetween(dentistId, start, end);
        
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                DentistError.ERR_DENTIST_VACATION_CONFLICT,
                EntityContext.DENTIST
            );
        }
        
        // 3. Aplicar cambio de estado
        Dentist dentist = dentistRepository.findById(dentistId)
            .orElseThrow(() -> new DentistNotFoundException(dentistId));
        
        dentist.applyVacation(start, end);
        dentistRepository.save(dentist);
    }
}
```

**Beneficios:**
- ✅ `Dentist` no conoce `Appointment`
- ✅ Validación centralizada y testeable
- ✅ Responsabilidad clara del servicio

---

#### B. Incapacidad (Imprevista) → Siempre permitida + Efectos secundarios

**Diferencia clave:** La incapacidad NO puede ser bloqueada por citas existentes.

**Ejemplo del problema real:**

```
Escenario:
- Dr. García tiene cita mañana a las 10:00 AM
- Hoy a las 8:00 PM sufre un accidente (pierna rota)
- Necesita incapacitarse inmediatamente

❌ INCORRECTO: "No puedes incapacitarte porque tienes cita mañana"
✅ CORRECTO: "Te incapacitas Y cancelamos/reprogramamos las citas"
```

**Implementación:**

```java
@Service
public class DentistIncapacityService {
    
    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;
    private final EventPublisher eventPublisher;
    
    /**
     * ✅ CORRECTO: Incapacidad siempre permitida + orquestación
     */
    @Transactional
    public void registerIncapacity(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end,
            String medicalNote) {
        
        // 1. Aplicar estado de incapacidad (SIEMPRE permitido)
        Dentist dentist = dentistRepository.findById(dentistId)
            .orElseThrow(() -> new DentistNotFoundException(dentistId));
        
        dentist.applyIncapacity(start, end, medicalNote);
        dentistRepository.save(dentist);
        
        // 2. Orquestar efectos secundarios: cancelar/reprogramar citas
        List<Appointment> affectedAppointments = appointmentRepository
            .findByDentistBetween(dentistId, start, end);
        
        for (Appointment appointment : affectedAppointments) {
            // Cancelar cita
            appointment.cancel("Dentist incapacitated: " + medicalNote);
            appointmentRepository.save(appointment);
            
            // Publicar evento para notificaciones
            eventPublisher.publish(new AppointmentCancelledDueToIncapacityEvent(
                appointment.getId(),
                dentistId,
                appointment.getPatientId(),
                start,
                end
            ));
        }
        
        // 3. Publicar evento de dominio
        eventPublisher.publish(new DentistIncapacitatedEvent(
            dentistId,
            start,
            end,
            affectedAppointments.size()
        ));
    }
}
```

**Beneficios:**
- ✅ Incapacidad siempre permitida (refleja realidad clínica)
- ✅ Efectos secundarios orquestados (cancelaciones)
- ✅ Eventos para notificaciones asíncronas
- ✅ Separación de responsabilidades

---

### 4. Agregado `Dentist` Expone Mutaciones Controladas

**El agregado NO valida, solo aplica:**

```java
public class Dentist {
    
    private DentistId id;
    private DentistAvailabilityStatus availabilityStatus;
    
    // Metadata de ausencia
    private LocalDateTime vacationStart;
    private LocalDateTime vacationEnd;
    private LocalDateTime incapacityStart;
    private LocalDateTime incapacityEnd;
    private String incapacityNote;
    
    /**
     * ✅ CORRECTO: Solo aplica cambio validado por servicio
     */
    public void applyVacation(LocalDateTime start, LocalDateTime end) {
        this.availabilityStatus = DentistAvailabilityStatus.of(Status.VACATION);
        this.vacationStart = start;
        this.vacationEnd = end;
        
        // Auditoría
        this.lastUpdated = Instant.now();
    }
    
    /**
     * ✅ CORRECTO: Solo aplica cambio, servicio orquesta efectos
     */
    public void applyIncapacity(LocalDateTime start, LocalDateTime end, String note) {
        this.availabilityStatus = DentistAvailabilityStatus.of(Status.SICK_LEAVE);
        this.incapacityStart = start;
        this.incapacityEnd = end;
        this.incapacityNote = note;
        
        // Auditoría
        this.lastUpdated = Instant.now();
    }
    
    /**
     * ✅ CORRECTO: Volver a disponible cuando termina ausencia
     */
    public void returnToAvailable() {
        this.availabilityStatus = DentistAvailabilityStatus.of(Status.AVAILABLE);
        this.vacationStart = null;
        this.vacationEnd = null;
        this.incapacityStart = null;
        this.incapacityEnd = null;
        this.incapacityNote = null;
        
        // Auditoría
        this.lastUpdated = Instant.now();
    }
    
    // ❌ ELIMINADO: validateVacationRequest()
    // ❌ ELIMINADO: changeAvailability()
}
```

**Principio:**
> "El agregado protege sus invariantes locales. El Domain Service coordina efectos entre agregados."

---

### 5. VO Simplificado e Inmutable

**Código final de `DentistAvailabilityStatus`:**

```java
package com.clinica.domain.vo.dentist;

import java.util.Objects;

/**
 * Value Object: Estado de disponibilidad del dentista
 * 
 * Solo representa ausencias clínicas. Los estados operativos
 * (en consulta, en pausa, fuera de turno) están cubiertos por
 * Shift y Appointment.
 * 
 * Estados:
 * - AVAILABLE: Disponible para atender pacientes
 * - SICK_LEAVE: Ausencia médica imprevista
 * - VACATION: Ausencia planificada
 */
public final class DentistAvailabilityStatus {

    public enum Status {
        AVAILABLE,      // Disponible para atender
        SICK_LEAVE,     // Incapacidad médica
        VACATION        // Ausencia planificada
    }

    private final Status current;

    private DentistAvailabilityStatus(Status current) {
        this.current = Objects.requireNonNull(current, "Status cannot be null");
    }

    /**
     * Factory method
     */
    public static DentistAvailabilityStatus of(Status status) {
        return new DentistAvailabilityStatus(status);
    }

    // Getters

    public Status getCurrent() {
        return current;
    }

    // Consultas semánticas

    public boolean isAvailable() {
        return current == Status.AVAILABLE;
    }

    public boolean isAbsent() {
        return current == Status.SICK_LEAVE || current == Status.VACATION;
    }
    
    public boolean isOnVacation() {
        return current == Status.VACATION;
    }
    
    public boolean isOnSickLeave() {
        return current == Status.SICK_LEAVE;
    }

    // Prioridad para asignación

    public enum Priority {
        NOT_ASSIGNABLE,  // Ausente
        HIGH             // Disponible
    }

    public Priority getPriority() {
        return isAvailable() ? Priority.HIGH : Priority.NOT_ASSIGNABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DentistAvailabilityStatus)) return false;
        DentistAvailabilityStatus that = (DentistAvailabilityStatus) o;
        return current == that.current;
    }

    @Override
    public int hashCode() {
        return Objects.hash(current);
    }

    @Override
    public String toString() {
        return current.name();
    }
}
```

**Características:**
-  Inmutable (final, sin setters)
-  Solo 3 estados esenciales
-  Prioridad con enum (no números mágicos)
-  Sin máquina de estados compleja (innecesaria)

---

## Alternativas Descartadas

### Alternativa 1: Mantener Validación en Agregado

**Argumento:** "El dentista debe validar sus propias vacaciones."

**Por qué se descartó:**
- Viola límites de agregado (requiere conocer `Appointment`)
- Dificulta testing (necesita mockear repositorios)
- Mezcla responsabilidades (validación + mutación + coordinación)

---

### Alternativa 2: Tratar Incapacidad Igual que Vacaciones

**Argumento:** "Ambas son ausencias, deberían validarse igual."

**Por qué se descartó:**
- **Semántica diferente:**
    - Vacaciones: planificadas, se pueden rechazar si hay conflictos
    - Incapacidad: imprevista, SIEMPRE debe permitirse
- **Efectos diferentes:**
    - Vacaciones: bloquean agenda futura
    - Incapacidad: cancelan/reprograman citas existentes
- **Realidad clínica:** Un dentista accidentado no puede esperar a "no tener citas"

---

### Alternativa 3: Mantener Estados Operativos en el VO

**Argumento:** "Es útil saber si el dentista está `IN_CONSULTATION` o `ON_BREAK`."

**Por qué se descartó:**
- **Redundancia con `Shift` y `Appointment`:**
    - `IN_CONSULTATION` → Ya cubierto por `Appointment` activo
    - `ON_BREAK` → Ya cubierto por `Shift.excludedBlocks`
    - `OFF_SHIFT` → Ya cubierto por ausencia de `Shift` activo
- **Fuente de verdad única (ADR-Cita-07):**
    - `Shift` es la única fuente de verdad temporal
    - No duplicar información en múltiples lugares
- **Complejidad innecesaria:**
    - Máquina de estados compleja sin valor de negocio
    - Difícil mantener consistencia entre `Shift`, `Appointment` y `Status`

---

## Consecuencias

### Positivas

1. **Claridad Arquitectónica**
    -  `Shift` = tiempo operativo
    -  `Appointment` = ocupación
    -  `DentistAvailabilityStatus` = ausencias clínicas
    -  Cada concepto tiene una responsabilidad única

2. **Separación de Responsabilidades**
    -  Agregados mantienen invariantes locales
    -  Domain Services coordinan efectos entre agregados
    -  No hay acoplamiento entre módulos

3. **Semántica Correcta**
    -  Vacaciones (planificadas) ≠ Incapacidad (imprevista)
    -  Validaciones en el lugar correcto
    -  Efectos secundarios orquestados

4. **Código Más Simple**
    -  VO con 3 estados vs 10 estados
    -  Sin máquina de estados compleja
    -  Sin validaciones en agregado

5. **Mejor Performance**
    -  Menos consultas redundantes
    -  Sin duplicación de información

6. **Realidad Clínica**
    -  Incapacidad siempre permitida (como debe ser)
    -  Cancelaciones automáticas (evita pacientes esperando)
    -  Notificaciones a pacientes afectados

---

### Negativas

1. **Trabajo de Migración**
    -  Crear `DentistVacationService`
    -  Crear `DentistIncapacityService`
    -  Refactorizar `Dentist` (eliminar validaciones)
   

2. **Complejidad Operativa**
    -  Orquestación de cancelaciones requiere políticas claras
    -  Notificaciones a pacientes requieren infraestructura
    -  Eventos de dominio requieren handlers

3. **Disciplina Requerida**
    -  Los use cases deben usar los servicios correctos
    -  No bypass con `changeAvailability()` directo


---

## Diagramas

### Flujo Anterior (Acoplado)

```
Usuario → Request Vacation
          ↓
   Dentist.validateVacationRequest()
          ↓ (consulta directamente)
   Schedule.getAppointments()  ❌ Acoplamiento
          ↓
   UserStatus.mustBeActive()   ❌ Autorización incorrecta
          ↓
   (No aplica cambio de estado) ❌ Incompleto
```

---

### Flujo Nuevo (Desacoplado)

```
┌─────────────────────────────────────────────┐
│  VACACIONES (Planificadas)                  │
└─────────────────────────────────────────────┘

Usuario → Request Vacation
          ↓
   DentistVacationService
          ↓ (consulta repositorio)
   AppointmentRepository.findByDentistBetween()  ✅ Desacoplado
          ↓
   ¿Conflictos?
          ├─> NO  → Dentist.applyVacation()  ✅ Aplica estado
          └─> SÍ  → Throw Exception          ✅ Rechaza

┌─────────────────────────────────────────────┐
│  INCAPACIDAD (Imprevista)                   │
└─────────────────────────────────────────────┘

Usuario → Register Incapacity
          ↓
   DentistIncapacityService
          ↓
   Dentist.applyIncapacity()  ✅ Siempre permite
          ↓
   AppointmentRepository.findByDentistBetween()
          ↓
   Cancelar cada cita  ✅ Orquesta efectos
          ↓
   EventPublisher.publish()  ✅ Notifica
```

---

## Lecciones Aprendidas

### 0. El Contexto Arquitectónico Determina la Validez de las Decisiones

**Reflexión más importante:**

> "Las decisiones técnicas no son 'correctas' o 'incorrectas' en el vacío. Son correctas o incorrectas **en relación a la arquitectura que las rodea**."

**Cronología del aprendizaje:**

```
Semana 1-4 (Sin módulos consolidados):
  Decisión: "Dentist valida sus propias vacaciones"
  Evaluación: ✅ Razonable dada la arquitectura actual
  Razón: No hay Domain Services, no hay Schedule consolidado

Semana 8-12 (Algunos módulos consolidados):
  Decisión: "Comentar validación hasta resolver Schedule"
  Evaluación: ⚠️ Reconozco problema parcial
  Razón: Veo acoplamiento pero no tengo solución completa

Semana 20-24 (ADR-09 completado):
  Decisión: "Migrar a Domain Service + diferenciar vacaciones/incapacidad"
  Evaluación: ✅ CORRECTO tras consolidación
  Razón: Ahora SÍ existe arquitectura clara de 3 capas
```

**Lo que aprendí:**

 **No adelantarse a la arquitectura:** Intentar la "solución perfecta" sin tener los módulos necesarios lleva a sobre-ingeniería prematura.

 **Refactorizar cuando el contexto cambia:** Una vez consolidados Schedule, UserIdentity y Authorization, el código antiguo dejó de ser "razonablemente cohesivo" y se convirtió en "fundamentalmente acoplado".

 **La deuda técnica consciente es válida:** Comentar código problemático hasta tener la arquitectura lista es mejor que forzar una solución incompleta.

 **Los errores en módulos tempranos son esperables:** El módulo de Actor fue uno de los primeros; es natural que acumule decisiones que luego hay que revisar.

---

### 1. Diferencia Semántica Entre Ausencias Planificadas e Imprevistas

**Reflexión:**
> "Vacaciones e incapacidad son ambas ausencias, pero tienen reglas de negocio **completamente diferentes**."

**Aprendizaje:**

| Característica | Vacaciones | Incapacidad |
|---------------|------------|-------------|
| **Planificación** | Anticipada | Imprevista |
| **Validación previa** | ✅ Debe revisar citas | ❌ No puede esperar |
| **Bloqueo** | Rechaza si hay conflictos | Siempre permite |
| **Efectos** | Bloquea agenda futura | Cancela citas existentes |
| **Ejemplo** | "Quiero vacaciones en julio" | "Me atropelló un carro hoy" |

**Lección:**
> "No todas las ausencias se modelan igual. La semántica del dominio dicta las reglas."

---

### 2. Estados Redundantes vs Fuente de Verdad Única

**Reflexión:**
> "Si `Shift` ya cubre el tiempo operativo, ¿para qué duplicar esa información en un VO?"

**Aprendizaje:**

**ANTES (Redundancia):**
```
Dentist.status = IN_CONSULTATION
Appointment.status = IN_PROGRESS
Shift.current = ACTIVE

¿Cuál es la verdad?
¿Qué pasa si están inconsistentes?
```

**DESPUÉS (Fuente única):**
```
Shift = Tiempo operativo (8:00-17:00, con breaks)
Appointment = Ocupación (10:00-11:00 atendiendo)
DentistAvailabilityStatus = Ausencias clínicas (VACATION, SICK_LEAVE)

Cada concepto tiene UNA responsabilidad
```

**Lección:**
> "Evita duplicar información. Define una fuente de verdad única para cada concepto temporal."

---

### 3. Límites de Agregado No Son Opcionales

**Reflexión:**
> "No es 'cohesivo' que `Dentist` conozca `Schedule`. Es **acoplamiento**."

**Aprendizaje:**

 **INCORRECTO:**
```java
public void validateVacationRequest(..., Schedule schedule) {
    List<Appointment> conflicts = schedule.getAppointments()...
}
```
Dentist depende de Schedule → Límite de agregado violado

 **CORRECTO:**
```java
@Service
public class DentistVacationService {
    public void requestVacation(DentistId id, ...) {
        List<Appointment> conflicts = appointmentRepository.find...
    }
}
```
Domain Service coordina entre agregados → Límites respetados

**Lección:**
> "Si tu agregado necesita consultar otro agregado, probablemente necesitas un Domain Service."

---

### 4. Value Objects Deben Ser Inmutables

**Reflexión:**
> "Un VO que muta su estado interno no es un VO, es una entidad disfrazada."

**Aprendizaje:**

 **INCORRECTO:**
```java
public boolean tryTransitionTo(Status next) {
    this.current = next;  // ❌ Mutación
    return true;
}
```

 **CORRECTO (si se necesitan transiciones):**
```java
public DentistAvailabilityStatus transitionTo(Status next) {
    return new DentistAvailabilityStatus(next);  // ✅ Nueva instancia
}
```

**Pero en este caso:**
```java
// ✅ MEJOR: No necesitas transiciones en el VO
// El cambio de estado se maneja desde Domain Services
```

**Lección:**
> "Si tu VO tiene lógica de transiciones complejas, probablemente no es un VO."

---

### 5. Orquestación de Efectos Secundarios Requiere Domain Services

**Reflexión:**
> "Cambiar un estado es fácil. Coordinar cancelaciones, notificaciones y eventos es complejo."

**Aprendizaje:**

**ANTES:**
```java
dentist.changeAvailability(SICK_LEAVE);  // ❌ Solo cambia flag
// Las citas siguen activas ❌
// Los pacientes no son notificados ❌
```

**DESPUÉS:**
```java
incapacityService.registerIncapacity(dentistId, start, end, note);
// 1. Aplica estado ✅
// 2. Cancela citas ✅
// 3. Publica eventos ✅
// 4. Notifica pacientes (via event handler) ✅
```

**Lección:**
> "Los agregados protegen invariantes locales. Los Domain Services orquestan efectos entre agregados."

---

## Exhibición Profesional

Este ADR demuestra:

1. **Pensamiento Crítico Sostenido en el Tiempo**
    - Detectar problema inicial (acoplamiento en validación)
    - Reconocer que no tengo solución completa (comentar código)
    - Esperar al contexto adecuado (consolidar Schedule primero)
    - Aplicar solución correcta en el momento correcto

2. **Evolución Arquitectónica Documentada**
    - **Fase 1:** Validaciones en agregado (sin módulos → razonable)
    - **Fase 2:** Reconocimiento parcial (algunos módulos → insuficiente)
    - **Fase 3:** Solución completa (módulos consolidados → correcto)
    - **Resultado:** No es una corrección, es una **evolución**

3. **Comprensión de Semántica de Dominio**
    - Vacaciones ≠ Incapacidad (reglas diferentes)
    - Estados operativos ≠ Ausencias clínicas (responsabilidades diferentes)
    - Agregados ≠ Domain Services (coordinación diferente)

4. **Aplicación Práctica de Principios DDD**
    - Bounded Contexts: `Dentist` no conoce `Schedule`
    - Aggregate Boundaries: Límites claros y respetados
    - Domain Services: Coordinación de efectos entre agregados
    - Value Objects: Inmutables y sin lógica compleja

5. **Madurez Profesional Demostrada**
    - Documentar **por qué** decisiones iniciales parecían correctas
    - Explicar **cuándo** se hizo evidente que eran incorrectas
    - Mostrar **cómo** se detectó el problema (tras consolidar módulos)
    - Aplicar **solución correcta** en el momento adecuado

Este ADR es evidencia de **maduración arquitectónica sostenida**:

Este ADR, junto con **ADR-09 (Cita)**, muestra un patrón:
- Mismos errores en módulos diferentes (Actor vs Schedule)
- Misma causa raíz (módulos no consolidados)
- Misma solución (migrar a Domain Services)
- **Criterio arquitectónico ganado y aplicable**

---

## Relación con otros ADRs

### Inspirado por
- **ADR-09 (Cita):** Eliminación de validaciones redundantes de estado de usuario en agendamiento
    - Mismo problema: Validaciones en agregado que deberían estar en Domain Services
    - Mismo patrón: Acoplamiento entre módulos por consolidación tardía
    - Lección compartida: Domain Services coordinan, agregados protegen invariantes

### Complementa
- **ADR-(User)-02:** UserIdentity como agregado rico
    - `UserStatus` NO se valida en `Dentist`, se valida en autenticación
    - Separación clara entre autenticación y operaciones de negocio

- **ADR-(Arquitectura)-47:** Modelo híbrido RBAC/ABAC para autorización
    - Autorización valida permisos, NO estado ni disponibilidad
    - `DentistAvailabilityStatus` es estado de negocio, no de autorización

- **ADR-(Cita)-07:** Shift como única fuente de verdad temporal
    - `Shift` cubre tiempo operativo (breaks, turnos)
    - `DentistAvailabilityStatus` solo cubre ausencias clínicas
    - No duplicar información temporal

- **ADR-(Actores)-02:** Delegación de lógica Dentist a DomainService
    - Domain Services para coordinación compleja
    - Agregados solo para invariantes locales

### Supersede
- **Ningún ADR anterior** - Este es el 2 ADR sobre estado de disponibilidad del dentista
- Documenta correcciones a código escrito meses atrás

---

## Conclusión

Este ADR documenta la **evolución del diseño** del estado de disponibilidad del dentista desde:

**Fase Inicial:**
- Validaciones en agregado (parecía cohesivo)
- Muchos estados en el VO (intentando cubrir todo)
- Acoplamiento con otros módulos (sin arquitectura clara)

**Fase de Consolidación:**
- Reconocimiento parcial del problema (acoplamiento visible)
- Espera consciente (necesito consolidar Schedule primero)
- Aprendizaje de otros módulos (ADR-09 muestra el camino)

**Fase Final:**
- Solución completa implementada:
    -  Estados reducidos a 3 esenciales
    -  Domain Services para coordinación
    -  Diferenciación semántica vacaciones/incapacidad
    -  Límites de agregado respetados
    -  Efectos secundarios orquestados

**Decisiones clave:**
-  `DentistAvailabilityStatus` solo para ausencias clínicas
-  `Shift` para tiempo operativo
-  `Appointment` para ocupación
-  Domain Services para validación y coordinación
-  Vacaciones (planificadas) ≠ Incapacidad (imprevista)

Este cambio **simplifica el modelo, respeta límites de agregado, y refleja la realidad clínica** sin sacrificar funcionalidad.

---

**Nota final:** Este ADR es evidencia de **honestidad técnica y capacidad de aprendizaje sostenido**. No oculta los errores iniciales, sino que los documenta como parte natural de la evolución de un sistema complejo desarrollado incrementalmente. Reconocer que las decisiones iniciales eran razonables en su contexto, pero que deben revisarse cuando el contexto cambia, es una habilidad profesional valiosa que demuestra madurez arquitectónica.