# ADR-09 (Cita): Eliminación de validaciones redundantes de estado de usuario en agendamiento

- **Estado:** Aprobado
- **Fecha:** 2026-02-07
- **Autor:** David
- **Categoría:** Dominio - Lecciones Aprendidas
- **Supersede:** ADR-(Actores)-08, ADR-(Actores)-09 (parcialmente)

---

## Resumen Ejecutivo

Este ADR documenta la **evolución del diseño de validaciones de agendamiento** y la **eliminación de validaciones redundantes** que se implementaron cuando los módulos de autenticación y autorización **aún no estaban consolidados**.

**Contexto temporal crítico:** Las decisiones iniciales se tomaron en un momento donde:
-  El módulo de autenticación era básico (sin `UserIdentity` como agregado rico)
-  No existía el módulo de autorización (ni RBAC ni ABAC)
-  No había separación clara entre autenticación, autorización y lógica de negocio
-  No se comprendía completamente cómo debían interactuar estos módulos

En ese contexto de incertidumbre arquitectónica, **parecía razonable** validar el estado del usuario en cada operación de agendamiento como una "medida de seguridad". Sin embargo, una vez que los módulos de autenticación (**ADR-(User)-02**) y autorización (**ADR-47**) se consolidaron con sus responsabilidades bien definidas, se hizo evidente que estas validaciones eran **fundamentalmente incorrectas** y violaban principios de diseño.

**Decisión clave tras la consolidación:** Eliminar los métodos `DentistCanScheduleBetween.canScheduleBetween()` y `PatientCanScheduleBetween.canScheduleBetween()` del flujo de agendamiento, reconociendo que:

1. **Autenticación** ya valida el estado del usuario en el login
2. **Autorización** valida permisos, NO estado
3. **Operaciones de negocio** no deben repetir validaciones de capas superiores

Adicionalmente, se corrige la validación de `WorkingHours`, que debe ocurrir **al asignar el turno operativo (`Shift`)**, no al agendar la cita (`Appointment`).

---

## Contexto: La Evolución del Problema

**IMPORTANTE:** Las decisiones que aquí se documentan como "errores" fueron tomadas en un contexto arquitectónico completamente diferente al actual. En aquel momento, sin módulos de autenticación y autorización consolidados, **estas decisiones parecían razonables e incluso prudentes**. Solo después de completar la consolidación de estos módulos se hizo evidente que el enfoque inicial era fundamentalmente incorrecto.

### Fase 1: Primeros Intentos (Modelo Inmaduro) - **SIN módulos consolidados**

**Estado de la arquitectura en este punto:**
-  No existía `UserIdentity` como agregado rico
-  No había `UserAccessValidator` para autenticación
-  No existía módulo de autorización (ni RBAC ni ABAC)
-  No había separación clara de responsabilidades entre capas
-  La seguridad era una preocupación, pero sin arquitectura clara

**En ese contexto de incertidumbre, la decisión inicial fue:**

> "Si cada actor (Dentist, Patient) valida su propio estado de usuario antes de permitir agendamiento, estaremos seguros de que nadie inactivo puede crear citas."

**Esta lógica parecía sólida en su momento** porque:
1. No había un lugar centralizado para validar estado
2. No se comprendía completamente el flujo autenticación → autorización → negocio
3. Parecía una práctica "defensiva" razonable
4. Los ejemplos encontrados en tutoriales mezclaban estas responsabilidades

En las primeras iteraciones del módulo de citas, el agregado `UserIdentity` aún no estaba consolidado como agregado rico. El estado del usuario se delegaba a un Value Object `UserStatus` sin conexión real con el usuario del agregado correspondiente.

**Código inicial en `Patient.canScheduleBetween()`:**
```java
public void canScheduleBetween(UserIdentity user, LocalDateTime start, LocalDateTime end) {
    // ❌ PROBLEMA 1: UserStatus.from(user) no consultaba el usuario real
    UserStatus.from(user).mustBeActive(
        ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, 
        EntityContext.PATIENT
    );

    // ❌ PROBLEMA 2: Validación de Shift en Patient (responsabilidad incorrecta)
    if (shift == null) {
        throw new BusinessRuleViolationException(
            ErrorCatalogXD.ERR_PATIENT_NO_SHIFT_ASSIGNED, 
            EntityContext.PATIENT
        );
    }
    if (!shift.isAvailableBetween(start, end)) {
        throw new BusinessRuleViolationException(
            ErrorCatalogXD.ERR_PATIENT_SHIFT_NOT_AVAILABLE, 
            EntityContext.PATIENT
        );
    }
}
```

**Problemas identificados:**
1. **UserStatus no conectaba con el usuario real** del agregado → validación inútil
2. **Validación de `Shift` en `Patient`** → Patient no debe saber sobre turnos operativos
3. **Acoplamiento innecesario** entre Patient y conceptos de scheduling

**Código inicial en `Dentist.canScheduleBetween()`:**
```java
public void canScheduleBetween(UserIdentity user, LocalDateTime start, LocalDateTime end) {
    // ❌ PROBLEMA: Misma validación desconectada
    UserStatus.from(user).mustBeActive(
        ErrorCatalogXD.ERR_RECEPTIONIST_NOT_EDITABLE, 
        EntityContext.DENTIST
    );

    ensureEditable();
    
    // ⚠️ PROBLEMA: Validación en el momento incorrecto
    if (!workingHours.isWithinRange(start, end)) {
        throw new BusinessRuleViolationException(
            DentistError.ERR_DENTIST_OUT_OF_WORKING_HOURS, 
            EntityContext.DENTIST
        );
    }
}
```

**Problemas adicionales:**
- La validación de `WorkingHours` vs horario de cita es **conceptualmente incorrecta**
- Esta validación debe hacerse **al asignar el `Shift`**, no al agendar la cita
- El `Shift` ya debe haber sido validado contra `WorkingHours` previamente

---

### Fase 2: Consolidación de UserIdentity (Mejora Parcial) - **ADR-(User)-02**

**Cambio de paradigma:** En este punto, el módulo de autenticación se consolidó siguiendo **ADR-(User)-02: UserIdentity como agregado rico**. Esto cambió radicalmente el panorama:

- `UserIdentity` ahora era un agregado con comportamiento, no un POJO
- `canPerformSensitiveAction()` encapsulaba TODAS las validaciones de elegibilidad
- Se creó `UserAccessValidator` como anti-corruption layer

**Este fue el primer indicio de que la arquitectura inicial estaba mal**, porque ahora teníamos:
- Validación de estado en `UserIdentity.canPerformSensitiveAction()` ← **Correcto**
- Validación de estado en `Dentist.canScheduleBetween()` ← **Redundante pero aún no detectado**
- Validación de estado en `Patient.canScheduleBetween()` ← **Redundante pero aún no detectado**

Cuando se refactorizó el módulo de autenticación según **ADR-(User)-02**, se consolidó `UserIdentity` como agregado rico con el método `canPerformSensitiveAction()`.

**Implementación de `UserAccessValidator`:**
```java
public class UserAccessValidator {
    private final UserIdentityRepository userRepository;

    /**
     * Valida que un usuario existe y puede realizar acciones sensibles.
     *
     * Lanza excepciones de negocio si:
     * - El usuario no existe
     * - El usuario no está verificado
     * - El usuario está bloqueado
     * - El usuario está inactivo/suspendido
     */
    public void validateUserCanPerformSensitiveAction(
            UserId userId,
            Instant now,
            EntityContext requesterContext
    ) {
        // 1. Obtener usuario
        UserIdentity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIdentityNoFoundException(
                        UserIdentityError.ERR_USER_NOT_FOUND,
                        requesterContext, userId
                ));

        // 2. Validar elegibilidad (retorna Outcome del módulo técnico)
        Outcome<UserIdentity> eligibility = user.canPerformSensitiveAction(now);

        // 3. Traducir Outcome a Exception (anti-corruption layer)
        if (!eligibility.isSuccess()) {
            throw translateToBusinessException(
                    userId,
                    eligibility.getDetalles(),
                    requesterContext
            );
        }
    }
    
    private UserNotEligibleException translateToBusinessException(
            UserId userId,
            List<OutcomeDetail> details,
            EntityContext requesterContext
    ) {
        OutcomeDetail primaryDetail = details.get(0);
        String reason = buildReasonMessage(primaryDetail);
        
        return new UserNotEligibleException(
                userId,
                reason,
                requesterContext,
                details
        );
    }
    
    private String buildReasonMessage(OutcomeDetail detail) {
        return switch (detail.getCode().toString()) {
            case "ERR_USER_NOT_VERIFIED" ->
                    "Usuario no ha verificado su cuenta";
            case "ERR_USER_ACCOUNT_LOCKED" ->
                    "Cuenta de usuario bloqueada temporalmente";
            case "ERR_USER_INACTIVE" ->
                    "Usuario inactivo";
            case "ERR_USER_SUSPENDED" ->
                    "Usuario suspendido";
            default ->
                    "Usuario no elegible para realizar esta acción";
        };
    }
}
```

**Beneficios:**
-  Ahora sí se consulta el usuario real del agregado
-  Anti-corruption layer entre módulo técnico (Outcome) y dominio (Exceptions)
-  Validación centralizada y reutilizable

---

### Fase 3: Intento de Migración a Domain Service (Error Conceptual) - **Todavía sin módulo de autorización**

Con la validación de usuario funcionando (Fase 2), se intentó migrar los métodos `canScheduleBetween()` a Domain Services para evitar acoplamiento.

**En este punto aún no se había detectado el error fundamental** porque:
-  El módulo de autorización (RBAC/ABAC) aún no existía
-  No había separación conceptual clara entre autenticación y autorización
-  Se seguía pensando en "validar estado del usuario" como parte de la lógica de negocio

**Implementación de `DentistCanScheduleBetween`:**
```java
public class DentistCanScheduleBetween {
    private final DentistRepository dentistRepository;
    private final UserAccessValidator userAccessValidator;
    
    public DentistCanScheduleBetween(
            DentistRepository dentistRepository, 
            UserAccessValidator userAccessValidator) {
        this.dentistRepository = dentistRepository;
        this.userAccessValidator = userAccessValidator;
    }

    public void canScheduleBetween(
            UserIdentity user,
            LocalDateTime start, 
            LocalDateTime end) {
        
        Dentist dentist = dentistRepository.findByUserId(user.getId());
        
        // Validar usuario
        userAccessValidator.validateUserCanPerformSensitiveAction(
            user.getId(),
            Instant.now(),
            EntityContext.DENTIST
        );
        
        // Validar horario
        dentist.canScheduleBetween(start, end);
    }
}
```

**Uso en `AppointmentSchedulingService`:**
```java
public Appointment scheduleAppointment(...) {
    // ❌ PROBLEMA: Validación redundante
    dentistCanScheduleBetween.canScheduleBetween(user, start, end);
    patient.canScheduleBetween(user, start, end);
    
    ensureShiftCoverage(dentist.getId(), start, end);
    ensureAvailabilityCoverage(dentist.getId(), start, end);
    ensureNoConflicts(dentist.getId(), patient.getId(), start, end);
    
    // ...
}
```

**Nota importante:** Antes de migrar completamente, los métodos originales se **mantuvieron comentados** hasta revisar el módulo nuevamente.

---

## Problema: El Momento de la Epifanía - **Tras consolidar módulo de autorización (ADR-47)**

**Contexto del descubrimiento:**

Después de implementar el módulo de autorización completo según **ADR-47 (Modelo híbrido RBAC/ABAC)**, que incluía:
-  `RoleBasedPolicy` para permisos base (80% de casos)
-  `OwnershipPolicy`, `SectorBasedPolicy`, `SpecialtyBasedPolicy` (15% de casos)
-  `AuthorizationService` con flujo de evaluación claro
-  Separación conceptual entre autenticación (¿quién eres?) y autorización (¿qué puedes hacer?)

**Al revisar el código para completar la integración**, se hizo evidente una contradicción arquitectónica fundamental:

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
│    - Validación de WorkingHours ❌      │  ← AQUÍ estaba el problema
│    - Validación de estado ❌            │
└─────────────────────────────────────────┘
```

**La contradicción era obvia:**
- Si la **autenticación** ya valida estado en paso 1...
- Y la **autorización** valida permisos en paso 2...
- **¿Por qué las operaciones de negocio validaban estado OTRA VEZ en paso 3?**

Al revisar el código para completar la migración, se identificaron **tres errores fundamentales** que solo se hicieron evidentes con la arquitectura consolidada:

### Error 1: Validación Redundante de Estado de Usuario

**Observación crítica:**
> "Un usuario con estado INACTIVE no debería poder hacer login en el sistema."

**Realidad:**
-  La validación de `UserStatus` ya se realiza en la autenticación
-  Si un usuario está logueado, **por definición ya está activo**
-  Repetir esta validación en cada operación de agendamiento es **redundante**

**Impacto:**
- Se validaba estado en: login, agendar cita, reagendar cita, confirmar cita, cancelar cita
- Violación del principio **DRY (Don't Repeat Yourself)**
- Acoplamiento innecesario entre módulos

---

### Error 2: Validación de WorkingHours en el Momento Incorrecto

**Código problemático:**
```java
if (!workingHours.isWithinRange(start, end)) {
    throw new BusinessRuleViolationException(
        DentistError.ERR_DENTIST_OUT_OF_WORKING_HOURS, 
        EntityContext.DENTIST
    );
}
```

**¿Por qué es incorrecto?**

La validación de `WorkingHours` debe ocurrir **al asignar el `Shift`**, no al agendar la cita:

```
┌─────────────────────────────────────────────────┐
│          FLUJO CORRECTO                         │
└─────────────────────────────────────────────────┘

1. ASIGNAR TURNO OPERATIVO (Shift)
   ├─> Validar: WorkingHours.isWithinRange(shift.start, shift.end)
   ├─> Si válido: Crear Shift
   └─> Si inválido: Rechazar turno

2. AGENDAR CITA (Appointment)
   ├─> Validar: Shift.canAccommodateAppointment(appt.start, appt.end)
   ├─> Si válido: Crear Appointment
   └─> Si inválido: Rechazar cita

❌ INCORRECTO: Validar WorkingHours durante agendamiento
✅ CORRECTO: Validar WorkingHours al asignar Shift
```

**Razón:**
- El `Shift` es la "verdad operativa" del dentista
- Si el `Shift` existe, ya fue validado contra `WorkingHours`
- La cita solo debe preocuparse de caer **dentro del Shift**, no de validar el contrato laboral

---

### Error 3: Validación de Shift en Patient

**Código problemático:**
```java
// En Patient.canScheduleBetween()
if (shift == null) {
    throw new BusinessRuleViolationException(
        ErrorCatalogXD.ERR_PATIENT_NO_SHIFT_ASSIGNED, 
        EntityContext.PATIENT
    );
}
```

**¿Por qué es incorrecto?**
- `Patient` NO debe conocer sobre turnos operativos (`Shift`)
- `Shift` es un concepto del módulo de scheduling, no del módulo de actores
- Violación de **Separation of Concerns**

---

## Decisión

### 1. Eliminar Validaciones Redundantes

**Eliminar de `AppointmentSchedulingService`:**
```java
// ❌ ELIMINAR: Validación redundante
dentistCanScheduleBetween.canScheduleBetween(user, start, end);
patient.canScheduleBetween(user, start, end);
```

**Razón:**
- Si el usuario está logueado, ya pasó la validación de estado
- No hay valor en re-validar en cada operación

---

### 2. Eliminar Domain Services Innecesarios

**Eliminar completamente:**
- `DentistCanScheduleBetween`
- `PatientCanScheduleBetween`

**Razón:**
- Ya no tienen responsabilidad legítima
- Su única función era validar estado, que ahora es redundante

---

### 3. Mover Validación de WorkingHours al Momento Correcto

**Eliminar de `Dentist.canScheduleBetween()`:**
```java
// ❌ ELIMINAR: Validación en momento incorrecto
if (!workingHours.isWithinRange(start, end)) {
    throw new BusinessRuleViolationException(
        DentistError.ERR_DENTIST_OUT_OF_WORKING_HOURS
    );
}
```

**Implementar en `ShiftAssignmentService`:**
```java
public class ShiftAssignmentService {
    
    public Shift assignShift(
            Dentist dentist,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            ShiftType type) {
        
        // ✅ CORRECTO: Validar WorkingHours al asignar Shift
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        WorkingHours workingHours = dentist.getWorkingHoursForDay(dayOfWeek);
        
        if (!workingHours.isWithinRange(startTime, endTime)) {
            throw new BusinessRuleViolationException(
                ShiftError.ERR_SHIFT_OUTSIDE_WORKING_HOURS,
                EntityContext.SHIFT
            );
        }
        
        // Crear Shift validado
        Shift shift = Shift.create(
            ShiftId.generate(),
            dentist.getDentistId(),
            date,
            startTime,
            endTime,
            type
        );
        
        return shiftRepository.save(shift);
    }
}
```

---

### 4. Simplificar AppointmentSchedulingService

**Flujo simplificado:**
```java
public class AppointmentSchedulingService {

    public Appointment scheduleAppointment(
            Dentist dentist,
            Patient patient,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason,
            ProvidedService service,
            UserIdentity user) {
        
        // ✅ Única validación temporal: Shift cubre el intervalo
        Shift shift = ensureShiftCoverage(dentist.getDentistId(), start, end);
        
        // ✅ Detectar conflictos
        ensureNoConflicts(dentist.getDentistId(), patient.getPatientId(), start, end);
        
        // ✅ Crear cita
        Appointment appointment = buildAppointment(
                dentist.getDentistId(),
                patient.getPatientId(),
                service.getId(),
                start,
                end,
                type,
                reason
        );

        return appointmentRepository.save(appointment);
    }
    
    private Shift ensureShiftCoverage(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end) {

        if (!start.toLocalDate().equals(end.toLocalDate())) {
            throw new BusinessRuleViolationException(
                AppointmentError.ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS,
                EntityContext.APPOINTMENT
            );
        }

        List<Shift> shifts = shiftRepository.findActiveByDentistAndDate(
                dentistId,
                start.toLocalDate()
        );

        // ✅ Validación única: ¿El Shift cubre el intervalo?

        return shifts.stream()
                .filter(shift -> shift.canAccommodateAppointment(start, end))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException(
                        ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE,
                        EntityContext.SHIFT
                ));
    }
}
```

---

## Justificación

### 1. Validación de Estado es Responsabilidad de Autenticación, NO de Autorización

**Principio:** Separation of Concerns

```
┌─────────────────────────────────────────────────────────────┐
│     AUTENTICACIÓN (Login)                                   │
│  - Validar credenciales                                     │
│  - Validar estado de usuario (ACTIVE, VERIFIED, no LOCKED) │
│  - Cargar roles activos (según UserRolAssignment)          │
│  - Generar sesión/token con rol primario                   │
└─────────────────────────────────────────────────────────────┘
              ↓ Usuario autenticado con roles
┌─────────────────────────────────────────────────────────────┐
│     AUTORIZACIÓN (Por Operación)                            │
│  - Validar permisos según rol(es) del usuario              │
│  - Aplicar políticas RBAC base (RoleBasedPolicy)           │
│  - Aplicar políticas ABAC contextuales si aplican:         │
│    * OwnershipPolicy (¿es el dueño del recurso?)          │
│    * SectorBasedPolicy (¿es del sector correcto?)         │
│    * SpecialtyBasedPolicy (¿tiene la especialidad?)       │
│  (NO validan estado - ya pasó autenticación)               │
└─────────────────────────────────────────────────────────────┘
              ↓ Autorizado para operación
┌─────────────────────────────────────────────────────────────┐
│     OPERACIONES DE NEGOCIO                                  │
│  - Agendar cita                                            │
│  - Reagendar cita                                          │
│  - Cancelar cita                                           │
│  (NO validan estado ni permisos nuevamente)                │
└─────────────────────────────────────────────────────────────┘
```

**Razonamiento:**
- Si un usuario puede hacer login, **su estado ya fue validado en autenticación**
- Si llegó a una operación de negocio, **sus permisos ya fueron validados en autorización**
- Re-validar estado en cada operación de negocio es **triple redundancia**:
  1.  Primera validación: Autenticación (login) →  Correcta
  2.  Segunda validación: Autorización (permisos) →  No es su responsabilidad
  3.  Tercera validación: Operación de negocio → ️ Completamente redundante

**Confusión Original:**

En las primeras iteraciones, **confundí autorización con validación de estado**:

```java
// ❌ INCORRECTO: Validar estado en operación de negocio
public void canScheduleBetween(UserIdentity user, LocalDateTime start, LocalDateTime end) {
    // Esto es AUTENTICACIÓN, no lógica de agendamiento
    UserStatus.from(user).mustBeActive(...);
    
    // Esto SÍ es lógica de agendamiento
    if (!workingHours.isWithinRange(start, end)) { ... }
}
```

**Clarificación:**

Según **ADR-47 (RBAC/ABAC híbrido)**, la autorización se compone de:

1. **RoleBasedPolicy (80% de casos)**
  - Validación simple: ¿El rol tiene el permiso `CREATE_APPOINTMENT`?
  - Ejemplo: RECEPTIONIST puede crear citas, PATIENT no puede

2. **Contextual Policies (15% de casos)**
  - OwnershipPolicy: PATIENT solo puede editar SUS propios datos
  - SectorBasedPolicy: RECEPTIONIST solo puede eliminar dentistas de su sector
  - SpecialtyBasedPolicy: DENTIST solo ve servicios de su especialidad

3. **Business Operations (5% de casos)**
  - Permisos especiales: `COMPLETE_APPOINTMENT`, `APPROVE_INVOICE`

**NINGUNA de estas políticas valida el estado del usuario.**

¿Por qué? Porque **el estado ya fue validado en el login** mediante `UserIdentity.canPerformSensitiveAction()`.

**Implementación Correcta:**

```java
// ✅ AUTENTICACIÓN: Valida estado una sola vez
@PostMapping("/login")
public AuthResponse login(LoginRequest request) {
    UserIdentity user = userRepository.findByEmail(request.email());
    
    // 1. Validar credenciales
    if (!passwordEncoder.matches(request.password(), user.getHashedPassword())) {
        user.recordFailedLogin(Instant.now());
        throw new BadCredentialsException(...);
    }
    
    // 2. Validar elegibilidad (estado, verificación, bloqueo)
    Outcome<Void> eligibility = user.canPerformSensitiveAction(Instant.now());
    if (!eligibility.isSuccess()) {
        throw new UserNotEligibleException(...);
    }
    
    // 3. Cargar roles activos
    List<Rol> roles = userRolService.getActiveRoles(user.getId());
    Rol primaryRole = roles.stream()
        .filter(r -> r.isDefault())
        .findFirst()
        .orElse(roles.get(0));
    
    // 4. Generar token
    String token = jwtService.createToken(user.getId(), roles);
    
    user.recordSuccessfulLogin(Instant.now());
    
    return new AuthResponse(token, primaryRole.getRolEnum());
}
```

```java
// ✅ AUTORIZACIÓN: Solo valida permisos
@Around("@annotation(requiresPermission)")
public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission annotation) {
    CustomUserDetails userDetails = getCurrentUser();
    String requiredPermission = annotation.value(); // "CREATE_APPOINTMENT"
    
    // Construir contexto con datos del request
    SecurityContext context = SecurityContext.builder()
        .permission(Permission.of(requiredPermission))
        .requestingUserId(userDetails.getUserId())
        .build();
    
    // Validar: al menos UNO de los roles permite
    boolean authorized = userDetails.getRoles().stream()
        .anyMatch(rol -> authorizationService.isAuthorized(rol, context));
    
    if (!authorized) {
        throw new AccessDeniedException("User lacks permission: " + requiredPermission);
    }
    
    return joinPoint.proceed();
}
```

```java
// ✅ OPERACIÓN DE NEGOCIO: NO valida estado ni permisos
@Service
public class AppointmentSchedulingService {
    
    @Transactional
    @RequiresPermission("CREATE_APPOINTMENT") // ← Autorización vía AOP
    public Appointment scheduleAppointment(
            Dentist dentist,
            Patient patient,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentType type,
            String reason,
            ProvidedService service) {
        
        // NO HAY validación de UserStatus aquí ✅
        // Ya pasó autenticación (login)
        // Ya pasó autorización (AOP @RequiresPermission)
        
        // Solo validaciones de NEGOCIO:
        Shift shift = ensureShiftCoverage(dentist.getId(), start, end);
        ensureNoConflicts(dentist.getId(), patient.getId(), start, end);
        
        Appointment appointment = buildAppointment(...);
        return appointmentRepository.save(appointment);
    }
}
```

**Beneficio de Separación Clara:**

| Capa | Responsabilidad | Implementación |
|------|----------------|----------------|
| **Autenticación** | ¿Quién eres? ¿Estás activo? | `UserIdentity.canPerformSensitiveAction()` |
| **Autorización** | ¿Qué puedes hacer? | `AuthorizationService.isAuthorized()` |
| **Negocio** | ¿Cómo lo hacemos? | `AppointmentSchedulingService.schedule()` |

**Violaba el principio DRY:** Al validar estado en negocio, lo validaba 2 veces (autenticación + negocio)

---

### 2. WorkingHours es Validación de Asignación, No de Uso

**Analogía del mundo real:**

```
📋 CONTRATO LABORAL (WorkingHours)
   "Dr. Juan trabaja Lunes 8:00-17:00"
            ↓
🗓️ TURNO ASIGNADO (Shift)
   "Dr. Juan tiene turno 15-Feb 9:00-18:00"
   ❌ RECHAZADO: Excede jornada laboral
            ↓
🗓️ TURNO ASIGNADO (Shift)
   "Dr. Juan tiene turno 15-Feb 9:00-16:00"
   ✅ ACEPTADO: Dentro de jornada laboral
            ↓
📅 CITA (Appointment)
   "Paciente María, 15-Feb 10:00-11:00"
   ✅ ACEPTADO: Dentro del turno
```

**Momento correcto de validación:**
-  **INCORRECTO:** Validar WorkingHours al agendar cita
-  **CORRECTO:** Validar WorkingHours al asignar Shift

---

### 3. Patient No Debe Conocer Shift

**Principio:** Bounded Context Separation

```
┌─────────────────────────────────┐
│   MÓDULO: Actores               │
│                                 │
│   - Patient                     │
│   - Dentist                     │
│   - Guardian                    │
│   - Receptionist                │
│                                 │
│   Responsabilidad:              │
│   Modelar identidad clínica     │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│   MÓDULO: Scheduling            │
│                                 │
│   - Shift                       │
│   - Appointment                 │
│   - Availability (eliminado)    │
│                                 │
│   Responsabilidad:              │
│   Modelar operaciones de agenda │
└─────────────────────────────────┘
```

**Patient** vive en el módulo de **Actores**, no debe saber sobre conceptos de **Scheduling**.

---

## Alternativas Descartadas

### Alternativa 1: Mantener Validación de Estado "Por Seguridad"

**Argumento:** "¿Y si el usuario se desactiva entre login y agendamiento?"

**Por qué se descartó:**
- Escenario extremadamente improbable en sesión activa
- Si es una preocupación real, debe manejarse con:
  - **Session invalidation** cuando se desactiva un usuario
  - **Token revocation** en autenticación basada en JWT
- No con validaciones redundantes en cada operación

**Decisión:**
- Implementar invalidación de sesión al desactivar usuario
- NO validar estado en cada operación de negocio

---

### Alternativa 2: Mover WorkingHours a Shift

**Argumento:** "Si Shift siempre debe validar WorkingHours, ¿por qué no fusionarlos?"

**Por qué se descartó:**
- `WorkingHours` es un **contrato laboral** (recurrente, legal)
- `Shift` es una **asignación operativa** (puntual, flexible)
- Son conceptos separados en el dominio

**Ejemplo de separación legítima:**
```
WorkingHours: Lunes 8:00-17:00 (contrato de 40h/semana)
Shift hoy:    Lunes 9:00-15:00 (turno reducido por reunión)
```

---

### Alternativa 3: Validar WorkingHours en Appointment "Por Doble Seguridad"

**Argumento:** "¿Y si alguien crea un Shift inválido por error?"

**Por qué se descartó:**
- Esto es **validación defensiva**, no validación de negocio
- Si un Shift inválido existe, el problema está en `ShiftAssignmentService`
- La solución NO es validar en cada uso, sino **arreglar la asignación**

**Decisión:**
- Reforzar validaciones en `ShiftAssignmentService`
- NO duplicar validaciones en `AppointmentSchedulingService`

---

## Consecuencias

### Positivas

1. **Elimina Redundancia**
  -  Validación de estado solo en autenticación
  -  No se repite en cada operación
  -  Menos llamadas a repositorio

2. **Validaciones en el Momento Correcto**
  -  WorkingHours → al asignar Shift
  -  Shift → al agendar cita
  -  Coherencia conceptual

3. **Mejor Separación de Responsabilidades**
  -  Patient no conoce Shift
  -  Appointment no valida contratos laborales
  -  Cada módulo tiene su rol claro

4. **Código Más Simple**
  -  Menos Domain Services (eliminamos 2)
  -  Menos validaciones en AppointmentSchedulingService
  -  Flujo más legible

5. **Mejor Performance**
  -  Menos queries a UserIdentity
  -  Menos validaciones por operación

---

### Negativas

1. **Requiere Invalidación de Sesión**
  -  Al desactivar usuario, se debe invalidar sesión activa
  -  Requiere implementar session management adecuado

2. **Migración de Código**
  - ️ Eliminar `DentistCanScheduleBetween`
  - ️ Eliminar `PatientCanScheduleBetween`
  -    Crear `ShiftAssignmentService`
  -    Refactorizar `AppointmentSchedulingService`

3. **Documentación de Cambio Conceptual**
  -   Actualizar ADRs antiguos (ADR-(Actores)-08, 09)
  -    Documentar nueva responsabilidad de ShiftAssignmentService

---


## Diagramas

### Flujo Anterior (Redundante)

```
Usuario → Login
          ↓ (valida estado)
       Autenticado
          ↓
   Agendar Cita
          ↓
   DentistCanScheduleBetween
          ↓ (valida estado OTRA VEZ) ❌
   UserAccessValidator
          ↓
   Dentist.canScheduleBetween()
          ↓ (valida WorkingHours en momento incorrecto) ❌
   AppointmentSchedulingService
          ↓
   Crear Appointment
```

---

### Flujo Nuevo (Optimizado)

```
Usuario → Login
          ↓ (valida estado)
       Autenticado
          ↓
┌─────────────────────────────────┐
│  Asignar Shift (ANTES)          │
│                                 │
│  ShiftAssignmentService         │
│    ↓ (valida WorkingHours) ✅   │
│  Crear Shift                    │
└─────────────────────────────────┘
          ↓
┌─────────────────────────────────┐
│  Agendar Cita (DESPUÉS)         │
│                                 │
│  AppointmentSchedulingService   │
│    ↓ (valida Shift cubre cita) ✅│
│    ↓ (detecta conflictos) ✅    │
│  Crear Appointment              │
└─────────────────────────────────┘
```

---

## Lecciones Aprendidas

### 0. El Contexto Arquitectónico Importa Más que las Decisiones Puntuales

**Reflexión más importante:**

> "Las decisiones técnicas no son 'correctas' o 'incorrectas' en el vacío. Son correctas o incorrectas **en relación a la arquitectura que las rodea**."

**Cronología del aprendizaje:**

```
 (Sin módulos consolidados):
  Decisión: "Validar estado en cada operación de agendamiento"
  Evaluación: ✅ Razonable dada la arquitectura actual
  Razón: No hay lugar centralizado, mejor ser defensivo

 (UserIdentity consolidado):
  Decisión: "Mantener validación + usar UserAccessValidator"
  Evaluación: ⚠️ Redundante pero no obvio
  Razón: UserAccessValidator existe pero no se usa consistentemente

 (Autorización consolidada - ADR-47):
  Decisión: "Eliminar validaciones redundantes"
  Evaluación: ✅ CORRECTO tras consolidación
  Razón: Ahora SÍ existe arquitectura clara de 3 capas
```

**Lo que aprendí:**

 **No adelantarse a la arquitectura:** Intentar hacer "la solución correcta" sin tener los módulos consolidados lleva a sobre-ingeniería o diseños prematuros.

 **Refactorizar cuando el contexto cambia:** Una vez que los módulos de autenticación y autorización se consolidaron, el código antiguo dejó de ser "razonablemente defensivo" y se convirtió en "fundamentalmente incorrecto".

 **La deuda técnica a veces es necesaria:** El código con validaciones redundantes era "deuda técnica consciente" hasta que la arquitectura estuviera lista para soportar el diseño correcto.

---

### 1. La Importancia de Entender el Modelo Completo

**Reflexión personal:**
> "Cometí estos errores debido a mi poca experiencia al inicio del proyecto **y a la falta de una arquitectura consolidada**. No comprendía completamente cómo los módulos se relacionaban entre sí porque **esos módulos aún no existían de forma madura**."

**Lo que aprendí:**
-  Antes de agregar validaciones, entender **dónde ya existen** (pero primero deben existir)
-  Mapear el flujo completo de una operación antes de implementar
-  No asumir que "más validaciones = más seguridad"
-  Esperar a tener la arquitectura necesaria antes de implementar el diseño "ideal"

---

### 2. El Peligro de Validaciones "Por Si Acaso"

**Error común:**
> "Validar estado del usuario en cada operación por si acaso se desactivó entre login y agendamiento."

**Realidad:**
- Si es un problema real → implementar invalidación de sesión
- Si no es un problema real → no validar

**Principio:**
> **"Validate where it matters, not everywhere it could."**

---

### 3. El Momento Correcto de Validación

**Aprendizaje clave:**
> "No todas las validaciones deben ocurrir en el momento de uso. Algunas deben ocurrir en el momento de configuración."

**Ejemplos:**
- ❌ Validar WorkingHours al agendar cita
- ✅ Validar WorkingHours al asignar Shift

- ❌ Validar permisos de usuario en cada query
- ✅ Validar permisos en autenticación/autorización

---

### 4. Bounded Contexts y Separation of Concerns

**Error inicial:**
> "Patient validando Shift."

**Aprendizaje:**
- `Patient` vive en módulo de **Actores**
- `Shift` vive en módulo de **Scheduling**
- Nunca cruzar estos límites sin razón legítima

**Regla:**
> **"If an aggregate needs to know about a concept from another module, probably you're doing something wrong."**

---

### 5. El Valor de la Refactorización Consciente

**Estrategia que funcionó:**
> "Mantuve los métodos comentados en lugar de eliminarlos inmediatamente. Esto me permitió revisar el diseño completo antes de comprometer los cambios."

**Lección:**
-  No tener miedo de cambiar decisiones anteriores
-  Documentar el "por qué" de cada cambio (este ADR)
-  Aprender de los errores en lugar de ocultarlos

---

**Mensaje para reclutadores:**

Este ADR no es solo la documentación de una corrección de código. Es la evidencia de un proceso de **maduración arquitectónica** donde:

1. Se tomaron decisiones razonables con la información disponible
2. Se consolidó la arquitectura necesaria para soportar un mejor diseño
3. Se detectaron las limitaciones del diseño inicial
4. Se implementó la solución correcta en el momento correcto

---

## Relación con otros ADRs

### Supersede
- **ADR-(Actores)-08:** Delegación semántica para validar agendamiento
  - Estado: Superseded by ADR-(Cita)-09
  - Razón: Validación de estado es redundante, debe ocurrir solo en autenticación

- **ADR-(Actores)-09:** Refactorización semántica canScheduleAt(...)
  - Estado: Superseded by ADR-(Cita)-09
  - Razón: Método eliminado por completo, su única función era validar estado

### Complementa
- **ADR-(User)-02:** UserIdentity como agregado rico
  - `UserAccessValidator` sigue siendo válido para autenticación
  - Solo se elimina su uso redundante en operaciones de negocio
  - `UserIdentity.canPerformSensitiveAction()` es la ÚNICA validación de estado

- **ADR-(User)-04:** Validación de usuarios con UserAccessValidator
  - Sigue siendo la implementación correcta en la capa de autenticación
  - Anti-corruption layer entre módulo técnico (Outcome) y dominio (Exceptions)
  - Se usa en login, NO en cada operación de agendamiento

- **ADR-47 (Arquitectura):** Modelo híbrido RBAC/ABAC para autorización
  - **CLAVE:** Establece la diferencia entre autenticación y autorización
  - Autorización valida **permisos**, NO estado del usuario
  - RoleBasedPolicy: 80% casos (permisos base por rol)
  - Contextual Policies: 15% casos (ownership, sector, specialty)
  - Business Operations: 5% casos (operaciones especiales)
  - **NINGUNA política valida estado** - eso es responsabilidad de autenticación

- **ADR-01 (autorización):** Múltiples roles por usuario (UserRolAssignment)
  - Los usuarios pueden tener múltiples roles activos simultáneamente
  - Ejemplo: Dr. García es DENTIST + PATIENT
  - Rol primario se carga automáticamente en login
  - En autorización: basta que **UNO** de los roles permita la operación
  - Estado se valida UNA VEZ en login, aplica para TODOS los roles del usuario

**Implicación de Múltiples Roles:**

```java
// ✅ AUTENTICACIÓN: Valida estado una vez, carga todos los roles
@PostMapping("/login")
public AuthResponse login(LoginRequest request) {
    UserIdentity user = authenticate(request);
    
    // 1. Validar elegibilidad (UNA VEZ)
    Outcome<Void> eligibility = user.canPerformSensitiveAction(Instant.now());
    if (!eligibility.isSuccess()) {
        throw new UserNotEligibleException(...);
    }
    
    // 2. Cargar TODOS los roles activos
    List<Rol> roles = userRolService.getActiveRoles(user.getId());
    //     roles = [DENTIST, PATIENT]
    
    // 3. Identificar rol primario
    Rol primaryRole = roles.stream()
        .filter(Rol::isDefault)
        .findFirst()
        .orElse(roles.get(0));  // DENTIST
    
    // 4. Generar token con TODOS los roles
    String token = jwtService.createToken(user.getId(), roles);
    
    return new AuthResponse(token, primaryRole.getRolEnum());
}
```

```java
// ✅ AUTORIZACIÓN: Valida con CUALQUIERA de los roles
@Around("@annotation(requiresPermission)")
public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission annotation) {
    CustomUserDetails userDetails = getCurrentUser();
    //     roles = [DENTIST, PATIENT]
    
    String operation = annotation.value(); // "UPDATE_PATIENT"
    
    // Construir contexto
    SecurityContext context = SecurityContext.builder()
        .permission(Permission.of(operation))
        .requestingUserId(userDetails.getUserId())
        .attribute("resourceOwnerId", extractPatientId(joinPoint))
        .build();
    
    // ✅ Basta que UNO de los roles permita
    boolean authorized = userDetails.getRoles().stream()
        .anyMatch(rol -> authorizationService.isAuthorized(rol, context));
    
    if (!authorized) {
        throw new AccessDeniedException(...);
    }
    
    return joinPoint.proceed();
}
```

**Ejemplo concreto:**

```
Usuario: Dr. García
Roles: [DENTIST, PATIENT]
Operación: UPDATE_PATIENT (patientId=123)

Evaluación DENTIST:
  RoleBasedPolicy → ✓ DENTIST puede UPDATE_PATIENT
  OwnershipPolicy → ✗ 123 no es el Dr. García
  
  RESULTADO: ✗ DENEGADO

Evaluación PATIENT:
  RoleBasedPolicy → ✓ PATIENT puede UPDATE_PATIENT
  OwnershipPolicy → ✓ 123 ES el Dr. García (como paciente)
  
  RESULTADO: ✓ AUTORIZADO

RESULTADO FINAL: ✓ AUTORIZADO (al menos un rol permitió)
```

**Punto clave:** El estado del usuario se validó **UNA SOLA VEZ** en el login. No se re-valida en autorización ni en operaciones de negocio, sin importar cuántos roles tenga.

- **ADR-(Cita)-07:** Consolidación de Shift como única fuente de verdad
  - Coherente: Shift valida cobertura temporal
  - Shift debe validarse contra WorkingHours al crearse, NO al agendar cita

- **ADR-(Actores)-02:** Delegación de lógica Dentist a DomainService
  - Coherente: ShiftAssignmentService maneja asignación de turnos
  - Valida WorkingHours en el momento correcto

---

## Conclusión

Este ADR documenta la **evolución del diseño** desde una implementación inmadura con validaciones redundantes hacia un modelo más limpio y preciso. Los errores iniciales fueron resultado de:

1. Falta de comprensión del modelo completo de autenticación
2. Asunción incorrecta sobre dónde debían vivir las validaciones
3. Validación "defensiva" en lugar de validación de negocio

**La decisión final es clara:**
-  Validar estado de usuario **solo en autenticación**
-  Validar WorkingHours **al asignar Shift**
-  Validar cobertura de Shift **al agendar cita**
-  Eliminar Domain Services redundantes

Este cambio **simplifica el código, mejora la performance, y respeta los principios de diseño** sin sacrificar seguridad ni validaciones legítimas.

---

**Nota final:** Este ADR es también una **muestra de honestidad técnica** y **capacidad de aprendizaje**. Reconocer y documentar errores es una habilidad profesional valiosa que demuestra madurez y criterio ganado a través de la experiencia.