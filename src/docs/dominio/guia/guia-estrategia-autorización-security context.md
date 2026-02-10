
## Guia-estrategia-autorización-security context
**Última actualización:** 2026-02-04  
**Tipo:** Guía de implementación

---

## Resumen ejecutivo
Esta guía describe la **estrategia de autorización** basada en la construcción de un `SecurityContext` en la capa de aplicación y la evaluación por un `AuthorizationService` que combina políticas RBAC y ABAC. Incluye reglas prácticas sobre qué atributos incluir en el contexto, ejemplos de uso, diferencias entre atributos globales y de recurso, y decisiones de diseño (p. ej., resolver sector en dominio).

---

## Regla fundamental y flujo

**Regla:** La autorización se valida en la capa de aplicación construyendo un `SecurityContext` con atributos ABAC según el escenario.

**Flujo:**
```
Infraestructura (UserId autenticado) → Application Service
Application Service → SecurityContext (Permission + atributos)
SecurityContext → AuthorizationService → Políticas (RBAC + ABAC)
```

---

## Construcción del SecurityContext

**Patrón general**
```java
SecurityContext context = SecurityContext
    .builder(Permission.of(ResourceCatalog.of(...), ActionCatalog.of(...)), requesterId)
    .withSector(...)
    .withResourceId(...)
    .withResourceOwnerId(...)
    .build();
```

**Métodos de creación de Permission**
- **Atajos CRUD**: `Permission.create(...)`, `Permission.read(...)`, `Permission.update(...)`, `Permission.delete(...)` — útiles para operaciones estándar.
- **Acciones expresivas**: `Permission.of(ResourceCatalog.of(...), ActionCatalog.of(...))` — para acciones de negocio específicas (p. ej., `ROLE:CLONE`).

---

## Atributos del SecurityContext y cuándo usarlos

### Ownership
- **Función:** Garantizar que solo el propietario realice la operación.
- **Cuándo:** Recursos con dueño directo (ej. `Patient`).
- **Ejemplo**
```java
SecurityContext.builder(Permission.update(ResourceCatalog.of(PATIENT)), requesterId)
    .withResourceOwnerId(patient.getUser())
    .build();
```

### Sector
- **Función:** Restringir operaciones al sector/área del solicitante.
- **Cuándo:** Operaciones dependientes del área (ej. recepcionistas).
- **Ejemplo**
```java
builder.withSector("RECEPTION");
```

### Guardian
- **Función:** Validar que el solicitante es el guardián registrado.
- **Cuándo:** Recursos bajo responsabilidad de un guardián.
- **Ejemplo**
```java
builder.withPatientGuardianId(patient.getGuardianId());
```

---

## Atributos de seguridad globales vs atributos de recurso

### Seguridad global (UserId, RolId)
- **Definición:** Identifican al usuario autenticado y su rol activo.
- **Origen:** Infraestructura (`CustomUserDetails` vía `SecurityUtils`).
- **Uso:** Representan al **solicitante** en el `SecurityContext`.
- **Ejemplo**
```java
UserId requesterId = SecurityUtils.getCurrentUserId();
RolId requesterRolId = SecurityUtils.getCurrentUserRolId();
```

### Atributos de recurso (sector, especialidad, ownership)
- **Definición:** Propiedades del agregado (ej. `Recepcionista.sector`).
- **Origen:** Se obtienen al cargar el recurso desde el repositorio.
- **Uso:** Representan el **recurso objetivo** en el `SecurityContext`.
- **Ejemplo**
```java
Recepcionista recepcionista = recepcionistaRepository.findById(id).orElseThrow();
SecurityContext.builder(Permission.update(ResourceCatalog.of(RECEPCIONISTA)), requesterId)
    .withSector(recepcionista.getSector())
    .build();
```

### Diferencia crítica: solicitante vs recurso
- **Rol solicitante:** rol del usuario autenticado que intenta ejecutar la acción (`requesterRolId`).
- **Rol del recurso:** rol que corresponde al objeto sobre el que se actúa (`rol.getId()`).
- **Error común:** usar el `user.getId()` del recurso como si fuera el solicitante; esto concede permisos indebidos.

**Ejemplo correcto: editar usuario**
```java
User user = userIdentityRepository.findByUserId(UserId.of(userIdentityId));
UserId requesterId = SecurityUtils.getCurrentUserId();

SecurityContext context = SecurityContext
    .builder(Permission.update(ResourceCatalog.of(USER)), requesterId)
    .withResourceId(user.getId().value())
    .build();
```

---



## Rol activo y lista de roles
- **Decisión:** identificar un **rol activo** por sesión; usar ese `activeRolId` en el `SecurityContext`.
- **Justificación:** claridad semántica, auditoría precisa y seguridad (evita ambigüedad al evaluar permisos).

**CustomUserDetails**
```java
public class CustomUserDetails implements UserDetails {
    private final UserId id;
    private final List<Rol> rols;
    private final RolId activeRolId;
    public RolId getActiveRolId() { return activeRolId; }
}
```

---

## Resolver atributos en infraestructura vs dominio

| Enfoque | Cómo obtener sector | Ventajas | Desventajas | Cuándo usar |
|---------|---------------------|----------|------------|-------------|
| **Infraestructura** | `CustomUserDetails` | Simplicidad; menos repositorios | Riesgo de inconsistencia si cambia en dominio | Sistemas simples, sector estable |
| **Dominio** | Cargar `Receptionist` desde repositorio | Fuente de verdad; coherencia con dominio | Más llamadas/repositorios | Recomendado cuando sector es crítico y mutable |

**Decisión adoptada:** enfoque de dominio — `CustomUserDetails` solo contiene identidad y rol activo; sector se obtiene del agregado `Receptionist`.

---

## Estrategia por escenario (resumen)

| Escenario | Atributos añadidos | Política aplicada |
|-----------|--------------------|-------------------|
| Paciente | `withResourceOwnerId(patient.getUser())` | OwnershipPolicy |
| Recepcionista | `withSector("RECEPTION")` | SectorBasedPolicy |
| Guardián | `withPatientGuardianId(patient.getGuardianId())` | GuardianPolicy |
| Rol solicitante vs recurso | `requesterRolId` vs `rol.getId()` | RoleBasedPolicy |

---

## Buenas prácticas y recomendaciones
- Mantener `@RequiresPermission` como guardia declarativa y validar explícitamente con `AuthorizationService` para trazabilidad.
- Usar `Permission.of(...)` para acciones expresivas y `Permission.create/read/update/delete` para atajos CRUD.
- Construir `SecurityContext` en la capa de aplicación, combinando atributos globales (solicitante) y atributos de recurso (objetivo).
- Preferir resolver atributos críticos en dominio para mantener la fuente de verdad.
- Registrar auditoría con `action`, `resourceId`, `reason`, `requesterId`, `requesterRolId`, `timestamp`.

---

## Resumen visual

```
┌─────────────────────────────────────────────────────────────┐
│                  FLUJO DE AUTORIZACIÓN                      │
├─────────────────────────────────────────────────────────────┤
│ Infraestructura (UserId, RolId, Sector) → SecurityUtils     │
│ Application Service recibe atributos de seguridad           │
│ Construye SecurityContext                                   │
│   ├─ Permission (Resource + Action)                        │
│   ├─ requesterId (solicitante)                             │
│   ├─ resourceId (recurso objetivo)                         │
│   ├─ sector, guardian, owner                               │
│ AuthorizationService evalúa políticas                      │
│   ├─ OwnershipPolicy                                       │
│   ├─ SectorBasedPolicy                                     │
│   ├─ GuardianPolicy                                        │
│   └─ RoleBasedPolicy                                       │
│ Si todas permiten → ejecutar lógica de negocio             │
│ Si alguna niega → lanzar SecurityException                 │
└─────────────────────────────────────────────────────────────┘
```

