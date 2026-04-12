# ADR-47 (Arquitectura): Modelo híbrido RBAC/ABAC para autorización

- **Fecha**: 2026-02-01
- **Estado**: Aprobado
- **Categoría**: Seguridad / Arquitectura
- **Reemplaza**: Enfoque inicial de clases individuales por permiso
- **Complementa**: [ADR-37 (Arquitectura hexagonal)](ADR-%28Arquitectura%29-37-Arquitectura%20hexagonal%20para%20m%C3%B3dulo%20de%20acceso.md), [ADR-46 (JWT)](ADR%E2%80%91%28Arquitectura%29-46-Integraci%C3%B3n%20JWT%20y%20Spring%20Security%20en%20arquitectura%20hexagonal.md)
- **Autor:** David Stiven Sanclemente

---

## Contexto

### Situación Inicial

En la primera iteración del sistema de autorización, seguí un enfoque **granular por operación**:

```
permissions/
├── serviceDentist/
│   ├── CreateDentist.java
│   ├── ReadDentist.java
│   ├── UpdateDentist.java
│   ├── DeleteDentist.java
├── servicePatient/
│   ├── CreatePatient.java
│   ├── ReadPatient.java
│   └── ...
└── ... (11 agregados × 4 CRUD = 44+ clases)
```

Cada clase implementaba `PermissionPolicy`:

```java
public class DeleteDentist implements PermissionPolicy {
    @Override
    public String getCodigo() {
        return "DELETE_DENTIST";
    }
    
    @Override
    public boolean estaPermitido(Rol rol, ContextoAccion contexto) {
        return rol.getRolEnum() == RolEnum.RECEPTIONIST 
            && contexto.getSectorDestino().equals("RECURSOS_HUMANOS");
    }
}
```

### Problemas Identificados

1. **Explosión de clases**:
    - 11 agregados principales
    - 4 operaciones CRUD mínimo
    - Operaciones especiales (APPROVE, CANCEL, COMPLETE, etc.)
    - **Total proyectado: 60-100+ clases de políticas**

2. **Mantenimiento pesado**:
    - Cambiar permisos de un rol requiere modificar múltiples clases
    - Agregar nuevo recurso = crear 4+ clases nuevas
    - Difícil visualizar qué puede hacer cada rol

3. **Granularidad excesiva**:
    - No todos los recursos necesitan políticas complejas
    - La mayoría son simples checks RBAC
    - Solo ~15% requieren lógica ABAC (ownership, sector, especialidad)

4. **Inconsistencias**:
    - `PermissionPolicy.getCodigo()` retorna `PermissionPolicy` en lugar de `String`
    - `ContextoAccion` muy básico, solo tenía `sectorDestino`
    - Faltaba información para ownership, specialty, etc.

### El Problema Real

**No es un problema de autorización, es un problema de arquitectura.**

El enfoque granular funcionaría bien para un sistema con:
- Pocos recursos (5-10)
- Políticas muy complejas por recurso
- Requisitos de auditoría extremos

Pero mi sistema tiene:
- **Muchos recursos** (11+ agregados, creciendo)
- **Políticas simples** en su mayoría (80% RBAC puro)
- **Algunas políticas complejas** (ownership, sector, specialty)

---

## Decisión

Adoptar un **modelo híbrido RBAC/ABAC en 3 capas**:

### Capa 1: RBAC Base (80% de casos)
**Configuración centralizada de permisos por rol**

```java
public class RoleBasedPolicy implements PermissionPolicy {
    private static final Map<RolEnum, Set<Permission>> ROLE_PERMISSIONS = ...;
    
    static {
        // Toda la configuración RBAC en UN SOLO LUGAR
        configureReceptionistPermissions();
        configureDentistPermissions();
        configurePatientPermissions();
        configureGuardianPermissions();
    }
}
```

**Ventajas:**
- 1 clase para TODOS los permisos base
- Fácil de entender: "¿Qué puede hacer RECEPTIONIST? → Ver RoleBasedPolicy"
- Fácil de modificar: cambiar un Set, no 20 clases
- Exhibición clara para reclutadores

### Capa 2: ABAC Contextual (15% de casos)
**Políticas específicas para restricciones complejas**

```java
// Solo cuando REALMENTE necesito lógica compleja
public class OwnershipPolicy implements PermissionPolicy {
    // PATIENT solo puede UPDATE sus propios datos
    // GUARDIAN solo puede UPDATE pacientes bajo tutela
}

public class SectorBasedPolicy implements PermissionPolicy {
    // RECEPTIONIST solo puede DELETE DENTIST si es de RRHH
}

public class SpecialtyBasedPolicy implements PermissionPolicy {
    // DENTIST solo ve servicios de su especialidad
}
```

**Total: 3-5 políticas ABAC vs 100+ clases granulares**

### Capa 3: Operaciones de Negocio (5% de casos)
**Permisos especiales más allá de CRUD**

```java
Permission.of(Resources.APPOINTMENT, Actions.COMPLETE)
Permission.of(Resources.INVOICE, Actions.APPROVE)
Permission.of(Resources.JOURNAL_ENTRY, Actions.POST)
```

Configurados en la misma `RoleBasedPolicy`, pero con acciones específicas.

---

## Comparación: Antes vs Después

### Antes (Enfoque Granular)

**Estructura:**
```
permissions/
├── serviceDentist/
│   ├── CreateDentist.java      ← 50 líneas
│   ├── ReadDentist.java        ← 30 líneas
│   ├── UpdateDentist.java      ← 40 líneas
│   ├── DeleteDentist.java      ← 60 líneas (lógica de sector)
├── servicePatient/
│   ├── CreatePatient.java
│   ├── ReadPatient.java        ← 80 líneas (ownership)
│   ├── UpdatePatient.java      ← 80 líneas (ownership)
│   └── DeletePatient.java
└── ... (×11 agregados)
```

**Para saber qué puede hacer RECEPTIONIST:**
- Revisar 44+ archivos diferentes
- Buscar cada `rol.getRolEnum() == RECEPTIONIST`
- Sintetizar mentalmente la matriz completa

**Para agregar INVOICE:**
- Crear 4 nuevas clases
- Implementar cada una con lógica similar
- Registrar en algún lado (¿dónde?)

### Después (Modelo Híbrido)

**Estructura:**
```
policies/
├── RoleBasedPolicy.java        ← TODO RBAC aquí (200 líneas)
├── contextual/
│   ├── OwnershipPolicy.java    ← 80 líneas
│   ├── SectorBasedPolicy.java  ← 40 líneas
│   └── SpecialtyBasedPolicy.java ← 50 líneas
└── PermissionPolicy.java       ← Interface
```

**Total: ~370 líneas vs 2000+ líneas del enfoque granular**

**Para saber qué puede hacer RECEPTIONIST:**
```java
// Un solo método en RoleBasedPolicy
private static void configureReceptionistPermissions() {
    Set<Permission> permissions = new HashSet<>();
    
    // Gestión de actores
    permissions.add(Permission.create(PATIENT));
    permissions.add(Permission.read(PATIENT));
    permissions.add(Permission.update(PATIENT));
    permissions.add(Permission.delete(DENTIST)); // + SectorPolicy
    // ...
}
```

**Para agregar INVOICE:**
```java
// Agregar en Resources
public static final String INVOICE = "INVOICE";

// Agregar en RoleBasedPolicy
permissions.add(Permission.create(INVOICE));
permissions.add(Permission.read(INVOICE));
```

**Listo. No nuevas clases.**

---

## Arquitectura del Modelo Híbrido

### Componentes Principales

```
┌─────────────────────────────────────────────────────────────┐
│ Resources (Catálogo)                                         │
│ - PATIENT, DENTIST, INVOICE, APPOINTMENT...                 │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ Actions (Catálogo)                                           │
│ - CREATE, READ, UPDATE, DELETE, APPROVE, CANCEL...          │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ Permission (Value Object)                                    │
│ - resource: String                                           │
│ - action: String                                             │
│ - getCode(): String  // "UPDATE_PATIENT"                    │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ SecurityContext (Builder Pattern)                           │
│ - permission: Permission                                     │
│ - requestingUserIdentityId: UserId                                   │
│ - attributes: Map<String, Object>                           │
│   * resourceOwnerId                                          │
│   * sector                                                   │
│   * specialty                                                │
│   * patientGuardianId                                        │
└─────────────────────────────────────────────────────────────┘
                          │
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ AuthorizationService (Orquestador)                          │
│ - policies: List<PermissionPolicy>                          │
│ - isAuthorized(Rol, SecurityContext): boolean               │
└─────────────────────────────────────────────────────────────┘
                          │
            ┌─────────────┴─────────────┐
            ↓                           ↓
┌───────────────────────┐   ┌────────────────────────┐
│ RoleBasedPolicy       │   │ Contextual Policies    │
│ (Priority: 100)       │   │ (Priority: 200-300)    │
│                       │   │                        │
│ - RBAC Configuration  │   │ - OwnershipPolicy      │
│ - Simple checks       │   │ - SectorBasedPolicy    │
│                       │   │ - SpecialtyBasedPolicy │
└───────────────────────┘   └────────────────────────┘
```

### Flujo de Evaluación

```
Request: UPDATE_PATIENT (userIdentityId=123, patientId=123)
    │
    ↓
SecurityContext {
    permission: UPDATE_PATIENT
    userIdentityId: 123
    resourceOwnerId: 123
}
    │
    ↓
AuthorizationService.isAuthorized(PATIENT_ROLE, context)
    │
    ├─→ RoleBasedPolicy.appliesTo(context)? → true
    │   RoleBasedPolicy.isAllowed(PATIENT, context)? → true ✓
    │
    ├─→ OwnershipPolicy.appliesTo(context)? → true
    │   OwnershipPolicy.isAllowed(PATIENT, context)? 
    │   → userIdentityId == resourceOwnerId? → 123 == 123 → true ✓
    │
    └─→ SectorPolicy.appliesTo(context)? → false (no DELETE DENTIST)
        → SKIP
    
RESULT: ALL POLICIES ALLOWED → true
```

---

## Razonamiento

### ¿Por qué NO continuar con el enfoque granular?

#### 1. No escala
```
Año 1: 11 agregados × 4 CRUD = 44 clases
Año 2: 20 agregados × 5 ops  = 100 clases
Año 3: 30 agregados × 6 ops  = 180 clases
```

Cada clase necesita:
- Mantenimiento
- Tests
- Documentación
- Code review

#### 2. Dificulta la comprensión
Un reclutador viendo el proyecto pregunta:
- "¿Qué puede hacer un RECEPTIONIST?"
- Respuesta con granular: "Revisa estas 40 clases..."
- Respuesta con híbrido: "Mira este método de 20 líneas"

#### 3. Cambios son costosos
Cambiar permisos de RECEPTIONIST:
- Granular: Modificar 15+ clases, PR enorme
- Híbrido: Modificar 1 Set en 1 clase, PR de 5 líneas

#### 4. Testing es complejo
Granular:
```java
@Test testCreateDentistPermission() { ... }
@Test testReadDentistPermission() { ... }
@Test testUpdateDentistPermission() { ... }
@Test testDeleteDentistPermission() { ... }
// ×11 agregados = 44+ tests solo para RBAC
```

Híbrido:
```java
@Test testReceptionistPermissions() {
    // Validar todos los permisos en un solo test
    assertTrue(hasPermission(RECEPTIONIST, CREATE, PATIENT));
    assertTrue(hasPermission(RECEPTIONIST, READ, PATIENT));
    // ...
}
```

### ¿Por qué SÍ usar modelo híbrido?

#### 1. Escala naturalmente
Agregar recurso:
```java
// 1. Declarar en catálogo
public static final String NEW_RESOURCE = "NEW_RESOURCE";

// 2. Agregar a roles que lo necesitan
permissions.add(Permission.create(NEW_RESOURCE));
```

**2 líneas. No nuevas clases.**

#### 2. Políticas ABAC solo donde se necesitan
```java
// 80% de recursos: RBAC simple en RoleBasedPolicy
permissions.add(Permission.update(APPOINTMENT));

// 15% de recursos: + Ownership
if (resource == PATIENT && rol == PATIENT) {
    return OwnershipPolicy.check(...); // Clase separada
}

// 5% de recursos: + Sector
if (action == DELETE && resource == DENTIST) {
    return SectorPolicy.check(...); // Clase separada
}
```

Cada política compleja vive en su clase, pero solo las complejas.

#### 3. Fácil de testear
```java
@Test
void testRoleBasedPolicy() {
    // Test toda la matriz RBAC
    RoleBasedPolicy policy = new RoleBasedPolicy();
    
    // Receptionist
    assertTrue(policy.isAllowed(RECEPTIONIST, create(PATIENT)));
    assertTrue(policy.isAllowed(RECEPTIONIST, delete(DENTIST)));
    
    // Patient
    assertTrue(policy.isAllowed(PATIENT, read(PATIENT)));
    assertFalse(policy.isAllowed(PATIENT, delete(DENTIST)));
}

@Test
void testOwnershipPolicy() {
    // Test solo lógica de ownership
    SecurityContext ownData = context()
        .withUserId(123)
        .withResourceOwnerId(123)
        .build();
    
    assertTrue(ownershipPolicy.isAllowed(PATIENT, ownData));
}
```

#### 4. Exhibición profesional
Reclutador ve:
```java
// 1 clase RoleBasedPolicy con configuración clara
// 3 políticas ABAC específicas y bien nombradas
// 1 servicio AuthorizationService que orquesta
```

Conclusión: "Este desarrollador entiende arquitectura y pragmatismo"

vs

```java
// 100 clases de permisos dispersas
// Lógica duplicada en cada una
```

Conclusión: "¿Por qué hay tanto código repetitivo?"

---

## Consecuencias

### Ganamos

✅ **Escalabilidad**: Agregar recursos es trivial (2 líneas)

✅ **Mantenibilidad**: Cambios centralizados, PRs pequeños

✅ **Comprensibilidad**: Matriz de permisos visible de un vistazo

✅ **Testabilidad**: Tests enfocados, menos duplicación

✅ **Performance**: Evaluación eficiente (check de Set vs llamadas polimórficas)

✅ **Exhibición**: Demuestra madurez arquitectónica vs sobre-ingeniería

### Perdemos

⚠️ **Archivo grande**: `RoleBasedPolicy` puede crecer (200-300 líneas)
- **Mitigación**: Métodos privados por rol, bien organizados

⚠️ **Rigidez inicial**: Definir todos los roles upfront
- **Mitigación**: Iterativo, agregar roles según se necesiten

⚠️ **Curva de aprendizaje**: Entender el patrón de 3 capas
- **Mitigación**: Este ADR + diagramas + ejemplos

### Riesgos Mitigados

🚫 **Explosión de clases**: De 100+ a 5 clases

🚫 **Cambios costosos**: De 15 archivos a 1 archivo

🚫 **Tests duplicados**: De 100+ tests a 10-15 tests enfocados

---

## Alternativas Consideradas

### 1. RBAC Puro (sin ABAC)
```java
// Solo matriz rol→permisos, sin contexto
RECEPTIONIST → [CREATE_PATIENT, DELETE_DENTIST, ...]
```

**Rechazada:**
- No puede expresar "PATIENT solo actualiza SUS datos"
- No puede expresar "DENTIST solo ve servicios de SU especialidad"
- No puede expresar "RECEPTIONIST de RRHH puede eliminar dentistas"

### 2. ABAC Puro (sin RBAC)
```java
// Todo basado en atributos, sin roles
if (user.department == "RRHH" && action == "DELETE" && resource.type == "DENTIST")
```

**Rechazada:**
- Demasiado complejo para casos simples
- Difícil de auditar: "¿Quién puede hacer X?"
- Performance: evaluar atributos en cada request

### 3. Mantener Enfoque Granular
**Ya documentado arriba. Rechazado por no escalar.**

### 4. Policy Engine Externo (Oso, OPA, Casbin)
**Considerada pero rechazada para v1.0:**

Pros:
- Motor de reglas probado
- Sintaxis declarativa (Polar, Rego)

Contras:
- Dependencia externa crítica
- Curva de aprendizaje adicional
- Overkill para proyecto de portfolio
- Dificulta exhibición ("¿Sabes diseñar tu propio sistema?")

**Decisión**: Implementar híbrido propio. Si crece mucho, migrar a engine en v2.0.

---

## Implementación

### Fase 1: Catálogos (Completado)
```java
Resources.java    // PATIENT, DENTIST, INVOICE...
Actions.java      // CREATE, READ, UPDATE, DELETE...
Permission.java   // Value Object
SecurityContext.java // Builder con attributes
```

### Fase 2: RBAC Base (Completado)
```java
RoleBasedPolicy.java
    ├─ configureReceptionistPermissions()
    ├─ configureDentistPermissions()
    ├─ configurePatientPermissions()
    └─ configureGuardianPermissions()
```

### Fase 3: Políticas ABAC (Completado)
```java
OwnershipPolicy.java       // userIdentityId == resourceOwnerId
SectorBasedPolicy.java     // sector validation
SpecialtyBasedPolicy.java  // specialty validation
```

### Fase 4: Servicio de Autorización (Completado)
```java
AuthorizationService.java
    ├─ policies: List<PermissionPolicy>
    ├─ isAuthorized(Rol, SecurityContext)
    └─ hasPermission(Rol, UserId, resource, action)
```

### Fase 5: Integración Spring Security (Próximo)
- Ver ADR-39 para detalles
- `@RequiresPermission` annotation
- Aspect para validación automática
- Integration con JWT CustomUserDetails

---

## Métricas de Éxito

### Antes (Enfoque Granular)

| Métrica | Valor |
|---------|-------|
| Clases de políticas | 44+ (proyectado: 100+) |
| Líneas de código | ~2000+ |
| Tiempo para agregar recurso | 2-4 horas |
| Tests de permisos | 44+ |
| Complejidad cognitiva | Alta (dispersión) |

### Después (Modelo Híbrido)

| Métrica | Valor |
|---------|-------|
| Clases de políticas | 5 |
| Líneas de código | ~400 |
| Tiempo para agregar recurso | 5-10 minutos |
| Tests de permisos | 10-15 |
| Complejidad cognitiva | Media (centralizado) |

**Mejora**: 80% menos código, 95% menos tiempo, 70% menos tests

---

## Experiencia de Implementación

### Lo que Aprendí

**1:** Implementé 15 clases granulares. Funcionaba pero sentía redundancia.

**2:** Agregué `INVOICE`. Tardé 3 horas creando 5 clases casi idénticas.

**3:** Vi que con 11 agregados llegaría a 60+ clases. Busqué alternativas.

**4:** Diseñé modelo híbrido. Migré permisos existentes en 1 día.

**Resultado:** Sistema más simple, más mantenible, más profesional.

### Reflexiones

Al inicio pensaba: "Muchas clases pequeñas = mejor SRP"

Ahora entiendo: "Configuración centralizada ≠ violar SRP"

`RoleBasedPolicy` tiene UNA responsabilidad: definir matriz RBAC.

Las políticas ABAC tienen UNA responsabilidad cada una: evaluar contexto específico.

El problema no era SRP. Era granularidad excesiva.

---

## Relación con otros ADRs

**Complementa:**
- [ADR-(Arquitectura)-37-Arquitectura hexagonal para módulo de acceso.md](ADR-%28Arquitectura%29-37-Arquitectura%20hexagonal%20para%20m%C3%B3dulo%20de%20acceso.md)
- [ADR‑(Arquitectura)-46-Integración JWT y Spring Security en arquitectura hexagonal.md](ADR%E2%80%91%28Arquitectura%29-46-Integraci%C3%B3n%20JWT%20y%20Spring%20Security%20en%20arquitectura%20hexagonal.md)
- [ADR-(Arquitectura)-34-Separación save y update en puertos.md](ADR-%28Arquitectura%29-34-Separaci%C3%B3n%20save%20y%20update%20en%20puertos.md)
**Motiva:**
- [ADR-(Arquitectura)-39-Ubicación de validaciones de desactivación.md](ADR-%28Arquitectura%29-39-Ubicaci%C3%B3n%20de%20validaciones%20de%20desactivaci%C3%B3n.md)
- [ADR-(Arquitectura)-40-Estrategia Híbrida de Manejo de Errores - Outcome.md](ADR-%28Arquitectura%29-40-Estrategia%20H%C3%ADbrida%20de%20Manejo%20de%20Errores%20-%20Outcome.md)