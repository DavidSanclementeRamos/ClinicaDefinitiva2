
# Guía de autorización — Referencia de permisos por rol

**Última actualización:** 2026-04-11  
**Tipo:** Referencia de permisos (fuente de verdad)  
**ADRs relacionados:** [ADR-47 (modelo RBAC/ABAC)](../../architecture/decisions/arch/ADR-%28Arquitectura%29-47-Modelo%20h%C3%ADbrido%20RBAC%20y%20ABAC%20para%20autorizaci%C3%B3n.md), [ADR-51 (Authorization Helper)](../../architecture/decisions/arch/ADR-%28Arquitectura%29-51-Implementaci%C3%B3n%20de%20Authorization%20Helper%20Pattern.md)

---

## ⚠️ Este documento es la fuente de verdad del sistema de autorización

Cualquier discrepancia entre este documento y el código debe resolverse **corrigiendo el código para que se ajuste a lo especificado aquí**, a menos que exista una razón técnica documentada que justifique una implementación diferente y superior.

El proyecto ya no tiene mantenimiento activo por parte del autor original. Las correcciones pendientes están listadas al final como tareas para contribuidores.

---

## Cómo funciona el sistema de autorización

Una operación pasa por **tres filtros en cadena**. Fallar en cualquiera deniega el acceso:

```
Solicitud
    │
    ▼
┌──────────────────────────────────────────────────────┐
│  1. RoleBasedPolicy (RBAC)          prioridad 100    │
│     ¿Tiene el rol este permiso en su catálogo?       │
└──────────────────────────────────────────────────────┘
    │ sí
    ▼
┌──────────────────────────────────────────────────────┐
│  2. Políticas contextuales          prioridad 200    │
│     SectorBasedPolicy   → ¿sector del actor correcto?│
│     SpecialtyBasedPolicy → ¿especialidad coincide?   │
└──────────────────────────────────────────────────────┘
    │ sí (o no aplica al rol)
    ▼
┌──────────────────────────────────────────────────────┐
│  3. OwnershipPolicy                 prioridad 300    │
│     ¿El recurso pertenece al actor solicitante?      │
└──────────────────────────────────────────────────────┘
    │ sí (o no aplica al rol)
    ▼
  ✅ Permitido
```

`RoleBasedPolicy` aplica a todos los roles. Los demás son selectivos:
- `SectorBasedPolicy` → solo `RECEPTIONIST`
- `SpecialtyBasedPolicy` → solo `DENTIST`
- `OwnershipPolicy` → solo `PATIENT` y `GUARDIAN`

---

## Roles del sistema

| Rol | Responsabilidad principal |
|-----|--------------------------|
| `ADMINISTRATOR` | Gestión de accesos, identidades, contabilidad completa y catálogo de servicios |
| `RECEPTIONIST` | Operación clínica diaria. Sus permisos sensibles dependen del **sector asignado** |
| `DENTIST` | Atención clínica: citas y disponibilidad propia |
| `PATIENT` | Acceso a sus propios datos de contacto y consulta de citas/facturas |
| `GUARDIAN` | Gestión de sus datos y de los pacientes bajo su tutela |

---

## ADMINISTRATOR

Gestión completa del sistema de acceso, identidades, **módulo de contabilidad** y **catálogo de servicios odontológicos**. Solo lectura sobre entidades clínicas del módulo Actor.

| Recurso | Acciones |
|---------|----------|
| `ROLE` | READ, CREATE_CUSTOM, CLONE, ADD, REMOVE, SET_PERMISSIONS, CHECK, DELETE, ACTIVATE, DEACTIVATE, SUSPEND, MARK_DELETED |
| `ASSIGNMENT` | CREATE_TEMPORARY, CREATE_PERMANENT, REVOKE, REVOKE_ALL, EXTEND, READ, IS_ACTIVE_AT, IS_CURRENTLY_ACTIVE, UPDATE_PRIMARY, DELETE |
| `USER_IDENTITY` | CREATE, READ, UPDATE, ACTIVATE, DEACTIVATE, SUSPEND |
| `PATIENT` | READ |
| `DENTIST` | READ |
| `GUARDIAN` | READ |
| `RECEPTIONIST` | READ |
| `APPOINTMENT` | READ |
| `SHIFT` | READ |
| `AVAILABILITY` | READ |
| `PROVIDED_SERVICE` | CREATE, READ, UPDATE_INFORMATION, UPDATE_DETAILS, UPDATE_PRICE, ACTIVATE, DEACTIVATE |
| `INVOICE` | CREATE, READ, UPDATE, DELETE, APPROVE, REVERSE, POST |
| `PAYMENT` | CREATE, READ, UPDATE, DELETE |
| `RATE` | CREATE, READ, UPDATE, DELETE |
| `CONTRACT` | CREATE, READ, UPDATE, DELETE |
| `JOURNAL_ENTRY` | CREATE, READ, UPDATE, DELETE, REVERSE, POST |
| `COMPANY` | CREATE, READ, UPDATE, DELETE |
| `ADMINISTRATIVE_REPORT` | CREATE, READ, UPDATE, DELETE |

> El administrador es el **único rol** con acceso de escritura completo al módulo de contabilidad (`JOURNAL_ENTRY`, `COMPANY`, `RATE`) y gestión completa del catálogo de servicios (`PROVIDED_SERVICE`). El `RECEPTIONIST` accede a partes de estos módulos a través de los sectores `BILLING` y `ADMINISTRATION`.

---

## RECEPTIONIST

El rol operativo de la clínica. Tiene un catálogo amplio de permisos base, pero las **operaciones sensibles requieren el sector correcto**.

### Regla de evaluación

```
¿Tiene el permiso en su catálogo? (RoleBasedPolicy)
    └─ sí → ¿La operación está gateada por sector?
                └─ no → ✅ PERMITIDO
                └─ sí → ¿El sector del actor coincide con el requerido?
                            └─ sí → ✅ PERMITIDO
                            └─ no → ❌ DENEGADO
```

### Operaciones libres — cualquier sector

| Recurso | Acciones |
|---------|----------|
| `PATIENT` | CREATE, READ |
| `GUARDIAN` | CREATE, READ |
| `DENTIST` | READ |
| `RECEPTIONIST` | READ |
| `APPOINTMENT` | CREATE, READ, UPDATE, DELETE, CANCEL, RESCHEDULE, SCHEDULE |
| `SHIFT` | READ |
| `PROVIDED_SERVICE` | READ |
| `INVOICE` | READ |
| `PAYMENT` | READ |
| `RATE` | READ |
| `CONTRACT` | READ |
| `COMPANY` | READ |
| `ADMINISTRATIVE_REPORT` | READ |

### Operaciones gateadas por sector

#### Sector `HUMAN_RESOURCES` — gestión de personal

| Recurso | Acciones |
|---------|----------|
| `DENTIST` | CREATE, UPDATE_SENSITIVE, DELETE, SUSPEND |
| `RECEPTIONIST` | CREATE, UPDATE_SENSITIVE, DELETE, SUSPEND |
| `SHIFT` | CREATE, UPDATE, DELETE |

> `UPDATE_SENSITIVE` sobre `DENTIST` incluye: `Specialties`, `WorkingHours` y datos estructurales del profesional.  
> `UPDATE_SENSITIVE` sobre `RECEPTIONIST` incluye: `Sector` asignado y datos estructurales del actor.

#### Sector `BILLING` — gestión financiera

| Recurso | Acciones |
|---------|----------|
| `INVOICE` | CREATE, UPDATE, DELETE, APPROVE, REVERSE, POST |
| `PAYMENT` | CREATE, UPDATE, DELETE |
| `PROVIDED_SERVICE` | UPDATE_PRICE |

#### Sector `ADMINISTRATION` — configuración y contratos

| Recurso | Acciones |
|---------|----------|
| `COMPANY` | UPDATE |
| `CONTRACT` | CREATE, UPDATE, DELETE |
| `PROVIDED_SERVICE` | CREATE, UPDATE_DETAILS, UPDATE_INFORMATION |
| `ADMINISTRATIVE_REPORT` | CREATE, UPDATE, DELETE |
| `PATIENT` | REMOVE, ASSIGN |

### Actualizaciones de actores — desglose por tipo de datos

Las actualizaciones están divididas en dos categorías con reglas distintas. Esta es la parte más compleja del sistema y la más propensa a errores de implementación.

#### Datos de contacto — `UPDATE`

Datos mutables de baja sensibilidad: nombre visible, teléfono, dirección, email de contacto.

| Actor | Puede editar | Sector requerido para RECEPTIONIST |
|-------|-------------|-------------------------------------|
| `PATIENT` | El propio paciente (OwnershipPolicy) | `CUSTOMER_SERVICE`, `MEDICAL_RECORDS`, `CALL_CENTER`, `DENTAL_ASSISTANCE` |
| `GUARDIAN` | El propio guardián (OwnershipPolicy) | `CUSTOMER_SERVICE`, `MEDICAL_RECORDS`, `CALL_CENTER`, `DENTAL_ASSISTANCE` |
| `DENTIST` |  (OwnershipPolicy) | `HUMAN_RESOURCES` |
| `RECEPTIONIST` |  (OwnershipPolicy)  | `HUMAN_RESOURCES` |

#### Datos sensibles — `UPDATE_SENSITIVE`

Datos estructurales o de alta sensibilidad. **El propio actor no puede editarlos** — solo un recepcionista con el sector correcto.

| Actor | Datos sensibles | Sector requerido |
|-------|-----------------|------------------|
| `PATIENT` | `BloodType`, `DateOfBirth`, `Document` (DNI), `documentoEPS`, `FullName` | `MEDICAL_RECORDS` |
| `GUARDIAN` | `DateOfBirth`, `Document` (DNI), `FullName`, relaciones de tutela | `MEDICAL_RECORDS` |
| `DENTIST` | `Specialties`, `WorkingHours` | `HUMAN_RESOURCES` |
| `RECEPTIONIST` | `Sector` asignado, datos estructurales | `HUMAN_RESOURCES` |

**Regla crítica:** `OwnershipPolicy` no aplica para `UPDATE_SENSITIVE`. El propietario del recurso no tiene autorización para editar sus propios datos sensibles.

#### Implementación en Application Service

```java
// Datos de contacto
// → Propietario del recurso O RECEPTIONIST con sector correcto
authHelper.buildContext(
    requesterId, rolId,
    Permission.of(PATIENT, UPDATE),
    AuthorizationContext.builder()
        .withAttribute("sensitiveData", false)          // o ausente
        .withResourceOwnerId(patient.getUserIdentityId())
        .build()
);

// Datos sensibles de PATIENT o GUARDIAN
// → SOLO RECEPTIONIST con sector MEDICAL_RECORDS
authHelper.buildContext(
    requesterId, rolId,
    Permission.of(PATIENT, UPDATE_SENSITIVE),
    AuthorizationContext.builder()
        .withAttribute("sensitiveData", true)
        .withAttribute("sector", receptionist.getSector().getCode())
        .build()
);

// Datos sensibles de DENTIST o RECEPTIONIST
// → SOLO RECEPTIONIST con sector HUMAN_RESOURCES
authHelper.buildContext(
    requesterId, rolId,
    Permission.of(DENTIST, UPDATE_SENSITIVE),
    AuthorizationContext.builder()
        .withAttribute("sensitiveData", true)
        .withAttribute("sector", receptionist.getSector().getCode())
        .build()
);
```

---

## DENTIST

Gestión de su propia agenda clínica y disponibilidad. **No gestiona el catálogo de servicios** — esa responsabilidad corresponde al `ADMINISTRATOR` y al `RECEPTIONIST` con sector `ADMINISTRATION`.

| Recurso | Acciones | Restricción |
|---------|----------|-------------|
| `PATIENT` | READ | Solo pacientes asignados |
| `PROVIDED_SERVICE` | READ | Filtrado por especialidad (SpecialtyBasedPolicy) |
| `APPOINTMENT` | READ, COMPLETE, MARK_AS_NO_SHOW | Solo sus citas |
| `AVAILABILITY` | CREATE, READ, UPDATE, DELETE | Solo su disponibilidad |
| `SHIFT` | READ, UPDATE | Solo sus turnos |
| `INVOICE` | READ | — |
| `RATE` | READ | — |
| `ADMINISTRATIVE_REPORT` | READ | — |

> El dentista tiene **solo lectura** sobre `PROVIDED_SERVICE`. Puede consultar los servicios de su especialidad para orientar la atención clínica, pero no puede crear ni modificar el catálogo.

### Cómo funciona el filtro por especialidad

`SpecialtyBasedPolicy` evalúa los atributos del contexto:

```java
AuthorizationContext.builder()
    .withAttribute("dentistSpecialties", Set.of("ORTHODONTICS", "ORAL_SURGERY"))
    .withAttribute("serviceSpecialty", "ORTHODONTICS")
    .build()
```

Si los atributos no están presentes la política no restringe — la validación ocurre en la capa de negocio. Si están presentes, el dentista solo ve servicios cuya especialidad coincide con las suyas.

---

## PATIENT

Acceso a sus propios datos de contacto y consulta de recursos relacionados. No puede editar sus datos sensibles.

| Recurso | Acciones | Restricción |
|---------|----------|-------------|
| `PATIENT` | READ, UPDATE (datos de contacto) | Solo su propio registro (OwnershipPolicy) |
| `APPOINTMENT` | READ | Solo sus citas |
| `PROVIDED_SERVICE` | READ | — |
| `INVOICE` | READ | Solo sus facturas |
| `PAYMENT` | READ | Solo sus pagos |

---

## GUARDIAN

Acceso a sus propios datos de contacto y a los de los pacientes bajo su tutela.

| Recurso | Acciones | Restricción |
|---------|----------|-------------|
| `GUARDIAN` | READ, UPDATE (datos de contacto) | Solo sus propios datos (OwnershipPolicy) |
| `PATIENT` | READ, UPDATE (datos de contacto) | Solo pacientes bajo su tutela (OwnershipPolicy via guardianship) |
| `APPOINTMENT` | CREATE, READ, CANCEL | Solo citas de sus tutelados |
| `PROVIDED_SERVICE` | READ | — |
| `INVOICE` | READ | — |
| `PAYMENT` | READ | — |

---

## Comportamiento cuando faltan atributos en el contexto

| Política | Atributo ausente | Comportamiento |
|----------|-----------------|----------------|
| `SectorBasedPolicy` | `sector` | ❌ DENIEGA |
| `SectorBasedPolicy` | `sensitiveData` | Interpreta como `false` — aplica reglas de datos de contacto |
| `SpecialtyBasedPolicy` | especialidades | ✅ PERMITE — validación delegada a capa de negocio |
| `OwnershipPolicy` | `resourceOwnerId` o `patientGuardianId` | ❌ DENIEGA |

---

## Referencia rápida

| Operación | ADMIN | RECEPTIONIST | DENTIST | PATIENT | GUARDIAN |
|-----------|:-----:|:------------:|:-------:|:-------:|:--------:|
| Gestionar usuarios y roles | ✅ | — | — | — | — |
| Registrar paciente | — | ✅ libre | — | — | — |
| Editar datos de contacto de paciente | — | ✅ CS/MR/CC/DA | — | ✅ propio | ✅ tutelado |
| Editar datos sensibles de paciente | — | ✅ MEDICAL_RECORDS | — | ❌ | ❌ |
| Editar datos sensibles de dentista | — | ✅ HUMAN_RESOURCES | — | — | — |
| Gestionar catálogo de servicios | ✅ | ✅ ADMINISTRATION | — | — | — |
| Actualizar precio de servicio | ✅ | ✅ BILLING | — | — | — |
| Ver servicios | ✅ | ✅ | ✅ (su especialidad) | ✅ | ✅ |
| Crear/gestionar citas | — | ✅ libre | — | — | ✅ tutelado |
| Completar cita | — | — | ✅ propia | — | — |
| Cancelar cita | — | ✅ | — | — | ✅ |
| Gestionar disponibilidad | — | — | ✅ propia | — | — |
| Gestionar turnos | — | ✅ HUMAN_RESOURCES | ✅ propio | — | — |
| Crear factura / aprobar / revertir | ✅ | ✅ BILLING | — | — | — |
| Ver facturas | ✅ | ✅ | ✅ | ✅ propias | ✅ |
| Módulo de contabilidad completo | ✅ | — | — | — | — |
| Gestionar contratos | ✅ | ✅ ADMINISTRATION | — | — | — |

> **Leyenda sectores:** CS = CUSTOMER_SERVICE · MR = MEDICAL_RECORDS · CC = CALL_CENTER · DA = DENTAL_ASSISTANCE

---

## Tareas pendientes para contribuidores

### Tarea 1 — Eliminar uso innecesario de `AuthorizationHelper`

En los Application Services existen dos mecanismos de autorización:

- `@RequiresPermission` — interceptor AOP. **Obligatorio en todos los métodos que requieran autorización.**
- `AuthorizationHelper` — construye el `SecurityContext` con atributos ABAC. **Solo necesario cuando la operación tiene restricción de sector, ownership o especialidad.**

**El problema:** `AuthorizationHelper` se usó erróneamente en métodos que no necesitan contexto ABAC. No causa errores de seguridad, pero es código que no hace ningún trabajo real.

**Cómo identificar los métodos afectados:** usar este documento como referencia.

Operaciones que **no** necesitan `AuthorizationHelper`:
- `RECEPTIONIST` en cualquier operación libre de la tabla de arriba
- `DENTIST` leyendo `INVOICE`, `RATE`, `ADMINISTRATIVE_REPORT`
- `ADMINISTRATOR` en cualquier operación (no tiene políticas contextuales)

Operaciones que **sí** necesitan `AuthorizationHelper`:
- Cualquier operación gateada por sector en `RECEPTIONIST`
- `PATIENT:UPDATE` y `GUARDIAN:UPDATE` (ownership)
- `DENTIST` consultando `PROVIDED_SERVICE` (especialidad)

Para detalles de implementación, ver [guía de estrategia de autorización](../authorization/guia-estrategia-autorizacion-security-context.md).

---

### Tarea 2 — Validar implementación contra esta especificación

Verificar en `RoleBasedPolicy`, `SectorBasedPolicy`, `SpecialtyBasedPolicy` y `OwnershipPolicy`:

- `DENTIST` no tiene `CREATE` ni escritura sobre `PROVIDED_SERVICE` en `RoleBasedPolicy`
- `UPDATE_SENSITIVE` está correctamente separado de `UPDATE` en los Application Services de los cuatro actores
- Los sectores `CUSTOMER_SERVICE`, `MEDICAL_RECORDS`, `CALL_CENTER` y `DENTAL_ASSISTANCE` están mapeados en `SectorBasedPolicy` para `PATIENT:UPDATE` básico
- `MEDICAL_RECORDS` es el sector requerido para `UPDATE_SENSITIVE` de `PATIENT` y `GUARDIAN`
- `HUMAN_RESOURCES` es el sector requerido para `UPDATE_SENSITIVE` de `DENTIST` y `RECEPTIONIST`