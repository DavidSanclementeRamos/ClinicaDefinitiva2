

# ADR-001 (Autorización): Gestión de roles múltiples con UserRolAssignment

- **Fecha**: 2026-02-01
- **Estado**: Aprobado
- **Categoría**: Dominio / Seguridad
- **Complementa**: ADR-47 (RBAC/ABAC)

---

## Contexto

### Situación

En clínicas reales, una persona puede tener múltiples roles simultáneamente:

**Casos reales:**
- **Dr. García** es DENTIST y también es PATIENT (se atiende en la misma clínica)
- **Ana** es RECEPTIONIST y también GUARDIAN de su hijo
- **Pedro** es PATIENT y GUARDIAN de su padre

### Problema Original

En la primera iteración, modelé la relación usuario-rol como **1:1**:

```java
public class UserIdentity {
    private Rol rol;  // ← Solo UN rol
}
```

**Limitaciones:**
- Dr. García debe tener 2 cuentas (una como dentist, otra como patient)
- No puedo auditar "Dr. García como patient hizo X"
- Cambiar de rol requiere logout/login
- No puedo tener "rol temporal" (ej: ADMIN por 24h)

### Requisitos

1. **Múltiples roles simultáneos**: Un usuario puede tener N roles activos
2. **Rol primario**: Para UI, uno es el "principal" (default al login)
3. **Roles temporales**: Asignar rol con fecha de expiración
4. **Auditoría**: Saber qué rol usó el usuario en cada acción
5. **Performance**: No degradar validaciones de autorización

---

## Decisión

Introducir agregado `UserRolAssignment` para gestionar asignaciones rol-usuario:

### Modelo de Dominio

```
UserIdentity (1) ──────< (N) UserRolAssignment (N) >────── (1) Rol
                                                    
Un usuario puede tener múltiples asignaciones
Cada asignación relaciona usuario + rol + vigencia + prioridad
```

### Agregado UserRolAssignment

```java
public class UserRolAssignment {
    private Long id;
    private UserId userId;
    private Long rolId;
    private LocalDate validFrom;    // Inicio de vigencia
    private LocalDate validTo;      // Fin de vigencia (null = permanente)
    private boolean isPrimary;      // Rol principal (UI)
    
    // Factory methods
    static UserRolAssignment assignPermanent(UserId userId, Long rolId, boolean isPrimary);
    static UserRolAssignment assignTemporary(UserId userId, Long rolId, LocalDate from, LocalDate to);
    
    // Queries
    boolean isActiveAt(LocalDate date);
    boolean isCurrentlyActive();
    
    // Commands
    void extend(LocalDate newEndDate);
    void revoke();
}
```

### Domain Service: UserRolService

```java
public class UserRolService {
    
    // Queries
    List<Rol> getActiveRoles(UserId userId);
    Rol getPrimaryRole(UserId userId);
    
    // Commands
    UserRolAssignment assignRole(UserId userId, Long rolId, boolean isPrimary);
    UserRolAssignment assignTemporaryRole(UserId userId, Long rolId, LocalDate from, LocalDate to);
    void revokeRole(UserId userId, Long rolId);
    void revokeAllRoles(UserId userId);
}
```

---

## Razonamiento

### ¿Por qué NO tabla de join simple?

#### Opción A: Tabla Join (Rechazada)
```sql
CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);
```

**Problemas:**
- No puede expresar vigencia temporal
- No puede marcar rol primario
- No puede auditar cuándo se asignó
- Es infraestructura, no dominio

#### Opción B: UserRolAssignment (Elegida)
```java
public class UserRolAssignment {
    // Reglas de negocio:
    // - Validar fechas
    // - Solo un rol primario por usuario
    // - Vigencia temporal
    // - Auditoría completa
}
```

**Ventajas:**
- Es un agregado de dominio con reglas
- Puede evolucionar (agregar attributes, condiciones)
- Testeable independientemente
- Auditable

### ¿Por qué roles temporales?

**Casos de uso reales:**

1. **Rol de emergencia:**
   ```java
   // Dar ADMIN a soporte por 24h
   assignTemporary(supportUserId, ADMIN_ROLE, today, today.plusDays(1));
   ```

2. **Cobertura temporal:**
   ```java
   // Ana cubre a recepcionista de vacaciones
   assignTemporary(anaId, RECEPTIONIST, july1, july15);
   ```

3. **Período de prueba:**
   ```java
   // Dentist nuevo tiene permisos limitados por 3 meses
   assignTemporary(newDentistId, DENTIST_TRAINEE, today, today.plusMonths(3));
   ```

### ¿Por qué rol primario?

**Experiencia de usuario:**

Sin rol primario:
```
Login → ¿Qué rol quieres usar? [DENTIST] [PATIENT]
Cada pantalla → Selector de rol actual
```

Con rol primario:
```
Login → Automáticamente como DENTIST (rol primario)
Si necesita → Cambiar a PATIENT en menú
```

**Implementación:**
```java
@PostMapping("/login")
public AuthResponse login(LoginRequest request) {
    UserIdentity user = authenticate(...);
    
    // Cargar rol primario para sesión
    Rol primaryRole = userRolService.getPrimaryRole(user.getId());
    
    // Crear token con rol primario (+ otros roles si API necesita)
    String token = jwtService.createToken(user.getId(), Set.of(primaryRole));
    
    return new AuthResponse(token, primaryRole.getRolEnum());
}
```

---

## Integración con Autorización

### Evaluación con Múltiples Roles

```java
@Around("@annotation(requiresPermission)")
public Object checkPermission(...) {
    CustomUserDetails userDetails = getCurrentUser();
    
    // Usuario tiene roles: [PATIENT, GUARDIAN]
    List<Rol> roles = userDetails.getRoles();
    
    // Validar: al menos UNO de los roles permite
    boolean authorized = roles.stream()
        .anyMatch(rol -> authService.isAuthorized(rol, context));
    
    if (!authorized) {
        throw new AccessDeniedException(...);
    }
    
    return joinPoint.proceed();
}
```

**Comportamiento:**

```
Usuario: Pedro (PATIENT + GUARDIAN)
Operación: UPDATE_PATIENT (patientId=789)

Evaluación:
1. RoleBasedPolicy(PATIENT) → ✓ PATIENT puede UPDATE_PATIENT
2. OwnershipPolicy(PATIENT) → ✗ 789 != Pedro's ID
   
   RoleBasedPolicy(GUARDIAN) → ✓ GUARDIAN puede UPDATE_PATIENT
   OwnershipPolicy(GUARDIAN) → ✓ 789 es hijo de Pedro

RESULTADO: ✓ Autorizado (al menos un rol permitió)
```

### Auditoría por Rol

```java
@Transactional
public void updatePatient(UpdatePatientCommand cmd, Rol activeRol) {
    // Registrar qué rol usó
    auditService.record(new AuditEvent(
        userId: cmd.userId(),
        rolUsed: activeRol.getRolEnum(),  // ← GUARDIAN
        action: "UPDATE_PATIENT",
        resourceId: cmd.patientId()
    ));
    
    // Ejecutar operación
    patient.updateContactData(...);
}
```

---

## Consecuencias

### Ganamos

 **Flexibilidad real**: Usuarios pueden tener roles que necesitan

 **UX mejorado**: Rol primario simplifica navegación

 **Temporalidad**: Roles temporales para cobertura/emergencia

 **Auditoría completa**: Sabemos qué rol usó el usuario

 **Evolución**: Modelo puede crecer (conditions, scopes, etc.)

### Perdemos

️ **Complejidad**: Más entidades, más validaciones

️ **Performance**: Cargar múltiples roles por usuario

 **Queries complejas**: "Usuarios con rol X activo hoy"

### Mitigaciones

**Performance:**
```java
// Cache de roles por usuario
@Cacheable("user-roles")
public List<Rol> getActiveRoles(UserId userId) { ... }
```

**Complejidad:**
```java
// Helpers para casos comunes
userRolService.assignPermanent(userId, rolId, isPrimary);
// vs
new UserRolAssignment(userId, rolId, LocalDate.now(), null, isPrimary);
```

---

## Implementación

### Fase 1: Modelo de Datos

```sql
CREATE TABLE user_rol_assignments (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE,
    is_primary BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id),
    UNIQUE (user_id, rol_id, valid_from) -- Prevenir duplicados
);

CREATE INDEX idx_user_assignments ON user_rol_assignments(user_id);
CREATE INDEX idx_active_assignments ON user_rol_assignments(valid_from, valid_to);
```

### Fase 2: Domain Model

```java
UserRolAssignment.java   // Agregado
UserRolService.java      // Domain Service
UserRolAssignmentRepository.java  // Port
```

### Fase 3: Integración Spring Security

```java
CustomUserDetails {
    private List<Rol> roles;  // ← Multiple roles
    
    public Rol getPrimaryRole() {
        return roles.stream()
            .filter(Rol::isDefault)  // o custom flag
            .findFirst()
            .orElse(roles.get(0));
    }
}
```

### Fase 4: Migration de Datos Existentes

```java
@Component
public class RolAssignmentMigration {
    
    @Transactional
    public void migrateExistingUsers() {
        List<UserIdentity> users = userRepo.findAll();
        
        for (UserIdentity user : users) {
            // Migrar rol único a assignment permanente
            UserRolAssignment.assignPermanent(
                user.getId(),
                user.getRolId(),  // Asumiendo que existe
                true  // Es primario
            );
        }
    }
}
```

---

## Experiencia de Implementación

### Lo que Aprendí

**Semana 1:** Usuario-Rol era 1:1. Funcionaba pero limitado.

**Semana 2:** Cliente pidió "Dr. García es dentist Y patient". Necesité refactor.

**Semana 3:** Diseñé `UserRolAssignment`. Migré datos existentes.

**Semana 4:** Integré con Spring Security y AuthorizationService.

**Resultado:** Sistema flexible que refleja realidad clínica.

### Reflexiones

Al inicio evité múltiples roles por "simplicidad". Pero la simplicidad que no refleja el dominio es falsa simplicidad.

`UserRolAssignment` agregó complejidad técnica, pero **redujo** complejidad de negocio:

- No más "cuentas duplicadas"
- No más "logout/login para cambiar rol"
- Auditoría clara de quién hizo qué como quién

---

## Relación con otros ADRs

**Complementa:**
- ADR-47: RBAC/ABAC (valida con todos los roles activos)
- ADR-39: Spring Security (CustomUserDetails carga múltiples roles)
- ADR-37: Hexagonal (UserRolService es domain service)