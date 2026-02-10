# Descubrimiento de Reglas de Negocio - Módulo de Autorización

## Agregados: Rol, Permission, UserRolAssignment

## Propósito

Gestionar el sistema de autorización híbrido RBAC/ABAC de la clínica dental, controlando qué usuarios pueden realizar qué operaciones sobre qué recursos, considerando contextos dinámicos como ownership, sector y especialidad. Este módulo garantiza seguridad, auditoría y flexibilidad en la gestión de permisos sin reiniciar el sistema.

---

## CREACIÓN DE ROLES

### Roles de Sistema (No Editables)
- Los roles base (DENTIST, PATIENT, GUARDIAN, RECEPTIONIST) se crean al inicializar el sistema.
- No pueden ser editados ni eliminados (isEditable=false, isDeletable=false).
- Sus permisos están hardcoded en RoleBasedPolicy.
- Estado inicial: ACTIVE.
- Deben tener descripción clara que identifique su propósito.

### Roles Personalizados (Editables)
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede crear roles personalizados.
- Deben basarse en un RolEnum existente (RECEPTIONIST, DENTIST, etc.).
- Deben tener descripción única que los diferencie de otros roles.
- Pueden tener permisos personalizados almacenados en BD.
- Estado inicial: ACTIVE.
- Por defecto son editables y eliminables (isEditable=true, isDeletable=true).
- No pueden tener el mismo nombre que un rol de sistema.

---

## EDICIÓN / ACTUALIZACIÓN DE ROLES

### Roles de Sistema
- No pueden modificarse sus permisos (isEditable=false).
- Solo puede actualizarse su descripción para clarificación.
- No pueden cambiar su RolEnum base.
- No pueden marcarse como eliminables.

### Roles Personalizados
- Solo pueden editarse si isEditable=true.
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede modificarlos.
- Los permisos pueden agregarse/removerse dinámicamente.
- Los cambios se reflejan inmediatamente sin reiniciar sistema.
- Debe registrarse auditoría de cada cambio (quién, cuándo, qué).
- No pueden quedar sin permisos (mínimo 1 permiso).
- No pueden tener permisos duplicados.

---

## DESACTIVACIÓN / ELIMINACIÓN DE ROLES

### Restricciones Generales
- No puede eliminarse un rol si tiene usuarios asignados activamente.
- No puede eliminarse un rol de sistema (isDeletable=false).
- La eliminación es lógica (marca como INACTIVE), no física.
- Debe registrarse motivo obligatorio de eliminación.
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede eliminar.

### Proceso de Eliminación
1. Verificar que no tenga usuarios con UserRolAssignment activo.
2. Verificar que no sea rol de sistema.
3. Registrar motivo (mínimo 10 caracteres).
4. Marcar como INACTIVE en lugar de eliminación física.
5. Notificar a administradores del cambio.

---

## ASIGNACIÓN DE ROLES A USUARIOS (UserRolAssignment)

### Asignación Permanente
- Un usuario puede tener múltiples roles simultáneos.
- Cada asignación tiene validFrom pero validTo=null (permanente).
- Solo un rol puede ser primario (isPrimary=true) por usuario.
- Al asignar nuevo rol primario, se debe desprimarizar el anterior.
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede asignar roles.

### Asignación Temporal
- Permite asignar roles por tiempo limitado (ej: cobertura, emergencia).
- Requiere validFrom y validTo explícitos.
- Los roles temporales nunca son primarios (isPrimary=false).
- Se desactivan automáticamente al vencer validTo.
- Útil para roles administrativos temporales o permisos de prueba.

### Revocación de Roles
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede revocar.
- Establece validTo=fecha_actual para desactivar inmediatamente.
- No puede revocar el único rol activo de un usuario.
- Debe registrarse motivo de revocación.
- Si es rol primario, debe asignarse nuevo primario antes de revocar.

---

## GESTIÓN DE PERMISOS

### Estructura de Permission
- Cada permiso tiene resource (ej: PATIENT, DENTIST) y action (ej: CREATE, READ, UPDATE, DELETE).
- Los permisos son inmutables una vez creados.
- Se representan como Value Objects, no entidades.
- Formato código: ACTION_RESOURCE (ej: CREATE_PATIENT, DELETE_DENTIST).

### Agregar Permiso a Rol
- Solo aplicable a roles editables (isEditable=true).
- No puede agregar permiso duplicado.
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede agregar.
- Cambio se refleja inmediatamente en próxima validación.
- Se registra en auditoría.

### Remover Permiso de Rol
- Solo aplicable a roles editables.
- No puede dejar rol sin permisos (mínimo 1).
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede remover.
- Si el permiso no existe, operación es idempotente (no falla).
- Se registra en auditoría.

### Reemplazar Todos los Permisos
- Solo aplicable a roles editables.
- Debe proporcionar al menos 1 permiso nuevo.
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede ejecutar.
- Operación atómica: todo o nada.
- Se registra cambio completo en auditoría.

---

## VALIDACIÓN DE AUTORIZACIÓN (AuthorizationService)

### Evaluación RBAC (Capa 1)
- Verifica si el RolEnum tiene permiso base en RoleBasedPolicy.
- Para roles editables, consulta permisos en BD.
- Para roles no editables, usa configuración hardcoded.
- Si no pasa RBAC, deniega inmediatamente.

### Evaluación ABAC Contextual (Capa 2)
- **OwnershipPolicy**: Verifica que userIdentityId == resourceOwnerId.
- **SectorBasedPolicy**: Solo RECEPTIONIST de RECURSOS_HUMANOS puede DELETE DENTIST.
- **SpecialtyBasedPolicy**: DENTIST solo ve servicios de su especialidad.
- Cada política tiene prioridad (100-300), evalúa en orden descendente.
- Si alguna política aplicable deniega, bloquea operación.

### Construcción de SecurityContext
- Siempre requiere Permission (resource + action) y UserId.
- Atributos opcionales según validación:
    - resourceOwnerId: Para OwnershipPolicy
    - sector: Para SectorBasedPolicy
    - specialty: Para SpecialtyBasedPolicy
    - resourceId: Para validaciones específicas de recurso
- Builder pattern para construcción fluida.

---

## CLONACIÓN DE ROLES

### Propósito
- Crear variante de rol existente sin afectar el original.
- Permite personalizar permisos para casos de uso específicos.
- Facilita creación de roles especializados.

### Restricciones
- Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede clonar.
- Requiere descripción nueva (diferente del rol fuente).
- El rol clonado hereda permisos del original pero es independiente.
- El clon siempre es editable y eliminable.
- Se copia RolEnum base del rol fuente.

---

## OPERACIONES DE DOMINIO

### Rol
- **create(RolEnum, description, permissions)**: Crea rol personalizado.
- **addPermission(Permission)**: Agrega permiso si editable.
- **removePermission(Permission)**: Remueve permiso si editable.
- **setPermissions(Set<Permission>)**: Reemplaza todos los permisos.
- **hasPermission(Permission)**: Verifica si rol tiene permiso específico.
- **cloneRole(String newDescription)**: Clona rol con nueva descripción.
- **delete()**: Marca como INACTIVE (eliminación lógica).

### UserRolAssignment
- **assignPermanent(UserId, RolId, isPrimary)**: Asigna rol permanente.
- **assignTemporary(UserId, RolId, validFrom, validTo)**: Asigna rol temporal.
- **isActiveAt(LocalDate)**: Verifica si asignación es válida en fecha.
- **isCurrentlyActive()**: Verifica si asignación está activa hoy.
- **extend(LocalDate newEndDate)**: Extiende vigencia de rol temporal.
- **revoke()**: Desactiva asignación inmediatamente.

### AuthorizationService
- **isAuthorized(RolId, SecurityContext)**: Evalúa todas las políticas.
- **hasPermission(RolId, UserId, resource, action)**: Validación simple sin contexto.

---

## INVARIANTES GLOBALES

### Roles
- Un rol editable siempre tiene al menos 1 permiso.
- Un rol de sistema nunca puede ser editable ni eliminable.
- No pueden existir dos roles con la misma descripción.
- Los permisos de un rol no pueden tener duplicados.
- Un rol INACTIVE no puede asignarse a nuevos usuarios.

### UserRolAssignment
- Un usuario no puede tener dos asignaciones del mismo rol activas simultáneamente.
- Solo un rol puede ser primario por usuario en un momento dado.
- validFrom no puede ser posterior a validTo.
- Los roles temporales nunca son primarios.
- Un usuario debe tener al menos 1 rol activo en todo momento.

### Permisos
- Un Permission con mismo resource+action es considerado igual.
- Los permisos son inmutables (no cambian después de creados).
- Solo existen permisos válidos definidos en Resources y Actions.

---

## TRAZABILIDAD Y AUDITORÍA

### Registro de Cambios en Roles
- Cada modificación de permisos registra: usuario, fecha, operación, permisos afectados.
- La creación de rol personalizado registra: creador, fecha, permisos iniciales.
- La eliminación registra: eliminador, fecha, motivo.
- La clonación registra: clonador, fecha, rol fuente, permisos copiados.

### Registro de Asignaciones
- Cada asignación registra: asignador, fecha, usuario objetivo, rol asignado, vigencia.
- La revocación registra: revocador, fecha, motivo.
- La extensión de vigencia registra: extensor, fecha, nueva fecha fin.

### Registro de Autorizaciones
- Cada denegación registra: usuario, rol usado, recurso solicitado, acción solicitada, política que denegó.
- Las autorizaciones exitosas pueden registrarse opcionalmente para análisis.
- Se registra uso de cada política ABAC para métricas.

---

## REGLAS DESCUBIERTAS (Formato Estandarizado)

### ROL - Creación y Gestión

**RN-ROL-001**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede crear roles personalizados.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza operación con SecurityException.
- **Error asociado**: ERR_ROL_UNAUTHORIZED_CREATION

**RN-ROL-002**
- **Descripción**: Los roles personalizados deben tener descripción única.
- **Condición**: exists(Rol.description == newDescription)
- **Consecuencia**: Se rechaza creación.
- **Error asociado**: ERR_ROL_DUPLICATE_DESCRIPTION

**RN-ROL-003**
- **Descripción**: Un rol editable debe tener al menos 1 permiso.
- **Condición**: rol.isEditable && rol.permissions.isEmpty()
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_ROL_EMPTY_PERMISSIONS

**RN-ROL-004**
- **Descripción**: Los roles de sistema no pueden ser editados.
- **Condición**: !rol.isEditable && attempting modification
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_ROL_SYSTEM_NOT_EDITABLE

**RN-ROL-005**
- **Descripción**: No puede eliminarse un rol con usuarios asignados.
- **Condición**: UserRolAssignment.countActive(rolId) > 0
- **Consecuencia**: Se rechaza eliminación.
- **Error asociado**: ERR_ROL_HAS_ACTIVE_USERS

**RN-ROL-006**
- **Descripción**: La eliminación de rol requiere motivo obligatorio (mínimo 10 caracteres).
- **Condición**: deleteReason == null || deleteReason.length() < 10
- **Consecuencia**: Se rechaza eliminación.
- **Error asociado**: ERR_ROL_DELETE_REASON_REQUIRED

**RN-ROL-007**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede clonar roles.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza clonación.
- **Error asociado**: ERR_ROL_UNAUTHORIZED_CLONE

**RN-ROL-008**
- **Descripción**: La clonación requiere descripción nueva diferente del original.
- **Condición**: newDescription == sourceRol.description
- **Consecuencia**: Se rechaza clonación.
- **Error asociado**: ERR_ROL_CLONE_SAME_DESCRIPTION

**RN-ROL-009**
- **Descripción**: Los roles de sistema no pueden ser eliminados.
- **Condición**: !rol.isDeletable
- **Consecuencia**: Se rechaza eliminación.
- **Error asociado**: ERR_ROL_SYSTEM_NOT_DELETABLE

### PERMISSION - Gestión de Permisos

**RN-PERMISSION-001**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede agregar permisos.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_UNAUTHORIZED_ADD

**RN-PERMISSION-002**
- **Descripción**: No puede agregar permiso duplicado a un rol.
- **Condición**: rol.permissions.contains(newPermission)
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_ALREADY_EXISTS

**RN-PERMISSION-003**
- **Descripción**: No puede remover el último permiso de un rol editable.
- **Condición**: rol.permissions.size() == 1 && attempting remove
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_CANNOT_REMOVE_LAST

**RN-PERMISSION-004**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede remover permisos.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_UNAUTHORIZED_REMOVE

**RN-PERMISSION-005**
- **Descripción**: Al reemplazar permisos, debe haber al menos 1 permiso nuevo.
- **Condición**: newPermissions.isEmpty()
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_SET_EMPTY

**RN-PERMISSION-006**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede reemplazar permisos.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_PERMISSION_UNAUTHORIZED_SET

### USER-ROL-ASSIGNMENT - Asignación de Roles

**RN-ASSIGNMENT-001**
- **Descripción**: Un usuario no puede tener dos asignaciones del mismo rol activas.
- **Condición**: exists active UserRolAssignment(userIdentityId, rolId)
- **Consecuencia**: Se rechaza asignación.
- **Error asociado**: ERR_ASSIGNMENT_DUPLICATE_ACTIVE

**RN-ASSIGNMENT-002**
- **Descripción**: Solo un rol puede ser primario por usuario.
- **Condición**: exists active isPrimary=true && assigning new primary
- **Consecuencia**: Se desprimariza el anterior automáticamente.
- **Error asociado**: N/A (auto-corrección)

**RN-ASSIGNMENT-003**
- **Descripción**: Los roles temporales no pueden ser primarios.
- **Condición**: validTo != null && isPrimary == true
- **Consecuencia**: Se rechaza asignación.
- **Error asociado**: ERR_ASSIGNMENT_TEMPORARY_CANNOT_BE_PRIMARY

**RN-ASSIGNMENT-004**
- **Descripción**: validFrom no puede ser posterior a validTo.
- **Condición**: validFrom.isAfter(validTo)
- **Consecuencia**: Se rechaza asignación.
- **Error asociado**: ERR_ASSIGNMENT_INVALID_DATE_RANGE

**RN-ASSIGNMENT-005**
- **Descripción**: No puede revocar el único rol activo de un usuario.
- **Condición**: UserRolAssignment.countActive(userIdentityId) == 1
- **Consecuencia**: Se rechaza revocación.
- **Error asociado**: ERR_ASSIGNMENT_CANNOT_REVOKE_LAST

**RN-ASSIGNMENT-006**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede asignar roles.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza asignación.
- **Error asociado**: ERR_ASSIGNMENT_UNAUTHORIZED

**RN-ASSIGNMENT-007**
- **Descripción**: Solo RECEPTIONIST del sector RECURSOS_HUMANOS puede revocar roles.
- **Condición**: requester.rolEnum != RECEPTIONIST || requester.sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza revocación.
- **Error asociado**: ERR_ASSIGNMENT_UNAUTHORIZED_REVOKE

**RN-ASSIGNMENT-008**
- **Descripción**: No puede asignar rol INACTIVE.
- **Condición**: rol.status == INACTIVE
- **Consecuencia**: Se rechaza asignación.
- **Error asociado**: ERR_ASSIGNMENT_INACTIVE_ROLE

**RN-ASSIGNMENT-009**
- **Descripción**: La extensión de vigencia solo aplica a roles temporales.
- **Condición**: validTo == null && attempting extend
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_ASSIGNMENT_CANNOT_EXTEND_PERMANENT

**RN-ASSIGNMENT-010**
- **Descripción**: La nueva fecha fin debe ser posterior a la actual.
- **Condición**: newEndDate.isBefore(currentEndDate)
- **Consecuencia**: Se rechaza extensión.
- **Error asociado**: ERR_ASSIGNMENT_INVALID_EXTENSION_DATE

### AUTHORIZATION - Validación de Permisos

**RN-AUTH-001**
- **Descripción**: DELETE DENTIST solo por RECEPTIONIST del sector RECURSOS_HUMANOS.
- **Condición**: action=DELETE && resource=DENTIST && sector != "RECURSOS_HUMANOS"
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_AUTH_SECTOR_REQUIRED

**RN-AUTH-002**
- **Descripción**: PATIENT solo puede UPDATE sus propios datos.
- **Condición**: rolEnum=PATIENT && resource=PATIENT && userIdentityId != resourceOwnerId
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_AUTH_OWNERSHIP_REQUIRED

**RN-AUTH-003**
- **Descripción**: GUARDIAN solo puede UPDATE pacientes bajo su tutela.
- **Condición**: rolEnum=GUARDIAN && resource=PATIENT && patientGuardianId != userIdentityId
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_AUTH_GUARDIANSHIP_REQUIRED

**RN-AUTH-004**
- **Descripción**: DENTIST solo puede ver servicios de su especialidad.
- **Condición**: rolEnum=DENTIST && resource=PROVIDED_SERVICE && !hasSpecialty(serviceSpecialty)
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_AUTH_SPECIALTY_REQUIRED

**RN-AUTH-005**
- **Descripción**: Si no tiene permiso RBAC base, denegar inmediatamente.
- **Condición**: !RoleBasedPolicy.isAllowed(rol, permission)
- **Consecuencia**: Se rechaza operación.
- **Error asociado**: ERR_AUTH_PERMISSION_DENIED

---

## RELACIÓN CON ADRs

- **ADR-38**: Modelo híbrido RBAC/ABAC - decisión arquitectónica central
- **ADR-39**: Integración Spring Security con AuthorizationService
- **ADR-40**: Gestión de roles múltiples con UserRolAssignment
- **ADR-49**: Parámetros explícitos (UserId, RolId) en casos de uso

---

## EVENTOS DE DOMINIO

### Rol
- **RolCreated**: Al crear rol personalizado
- **RolPermissionAdded**: Al agregar permiso
- **RolPermissionRemoved**: Al remover permiso
- **RolPermissionsReplaced**: Al reemplazar todos los permisos
- **RolCloned**: Al clonar rol
- **RolDeleted**: Al marcar como INACTIVE

### UserRolAssignment
- **RolAssigned**: Al asignar rol a usuario
- **RolRevoked**: Al revocar rol
- **RolExtended**: Al extender vigencia de rol temporal
- **PrimaryRolChanged**: Al cambiar rol primario

### Authorization
- **AuthorizationDenied**: Al denegar operación (con detalles de política)
- **AuthorizationGranted**: Al permitir operación (opcional, para métricas)

---

## VALUE OBJECTS INVOLUCRADOS

### Permission (Value Object)
- **resource**: String (ej: "PATIENT", "DENTIST")
- **action**: String (ej: "CREATE", "READ", "UPDATE", "DELETE")
- **getCode()**: String (ej: "CREATE_PATIENT")
- Inmutable
- Equals basado en resource + action

### RolId (Value Object)
- **value**: Long
- Validación: no nulo, > 0
- Inmutable

### UserId (Value Object - del módulo User)
- **value**: Long
- Validación: no nulo, > 0
- Inmutable

### SecurityContext (Builder)
- **permission**: Permission
- **requestingUserIdentityId**: UserId
- **attributes**: Map<String, Object>
- Builder pattern para construcción fluida
- Atributos opcionales: resourceOwnerId, sector, specialty, resourceId

---

## EJEMPLO DE USO

```java
// ========== CREAR ROL PERSONALIZADO ==========
Rol receptionistRRHH = Rol.create(
    RolEnum.RECEPTIONIST,
    "Recepcionista RRHH",
    Set.of(
        Permission.create(Resources.DENTIST),
        Permission.delete(Resources.DENTIST)
    )
);
rolRepository.save(receptionistRRHH);

// ========== ASIGNAR ROL A USUARIO ==========
UserRolAssignment assignment = UserRolAssignment.assignPermanent(
    userIdentityId: UserId.of(42L),
    rolId: receptionistRRHH.getId(),
    isPrimary: true
);
assignmentRepo.save(assignment);

// ========== VALIDAR AUTORIZACIÓN ==========
SecurityContext context = SecurityContext
    .builder(Permission.delete(Resources.DENTIST), UserId.of(42L))
    .withSector("RECURSOS_HUMANOS")
    .build();

boolean allowed = authService.isAuthorized(
    receptionistRRHH.getId(),
    context
);
// true - tiene permiso y sector correcto

// ========== CLONAR ROL ==========
Rol receptionistNocturno = receptionistRRHH.cloneRole(
    "Recepcionista Nocturno"
);
rolRepository.save(receptionistNocturno);

// ========== AGREGAR PERMISO EN RUNTIME ==========
receptionistNocturno.addPermission(
    Permission.read(Resources.EMERGENCY_REPORT)
);
rolRepository.save(receptionistNocturno);
// ¡Sin reiniciar sistema!
```

---

## MÉTRICAS DE GESTIÓN

### Uso de Roles
- **Roles activos vs inactivos**: Cuántos roles personalizados están en uso
- **Usuarios por rol**: Distribución de usuarios en cada rol
- **Roles huérfanos**: Roles sin usuarios asignados (candidatos a eliminación)

### Autorización
- **Tasa de denegación**: % de operaciones denegadas por política
- **Política más restrictiva**: Cuál política ABAC deniega más frecuentemente
- **Operaciones más bloqueadas**: Qué resource+action se bloquean más

### Auditoría
- **Cambios de permisos por día**: Frecuencia de modificaciones
- **Asignaciones temporales activas**: Cuántos usuarios tienen roles temporales
- **Roles clonados**: Cuántos roles son variantes de otros

---

## SECTORES RECONOCIDOS

Para el contexto de RECEPTIONIST con validación por sector:

- **RECURSOS_HUMANOS**: Gestión de personal (puede DELETE DENTIST)
- **ADMISION**: Registro de pacientes
- **FACTURACION**: Gestión financiera
- **ARCHIVO**: Gestión de historias clínicas
- **CAJA**: Cobros y pagos
- **AGENDA**: Solo gestión de citas

---

## ESPECIALIDADES RECONOCIDAS

Para el contexto de DENTIST con validación por especialidad:

- **ORTODONCIA**: Corrección de malposiciones
- **ENDODONCIA**: Tratamiento de conductos
- **PERIODONCIA**: Tratamiento de encías
- **CIRUGIA_ORAL**: Procedimientos quirúrgicos
- **ODONTOPEDIATRIA**: Atención infantil
- **PROSTODONCIA**: Rehabilitación con prótesis
- **IMPLANTOLOGIA**: Colocación de implantes
- **ESTETICA_DENTAL**: Procedimientos estéticos
- **ODONTOLOGIA_GENERAL**: Atención integral

---

## JUSTIFICACIÓN SEMÁNTICA

Este módulo implementa un sistema de autorización profesional que balancea:

1. **Seguridad**: Validación multi-capa (RBAC + ABAC) previene accesos no autorizados
2. **Flexibilidad**: Roles personalizados y permisos dinámicos permiten adaptación sin código
3. **Auditoría**: Registro completo de cambios y decisiones de autorización
4. **Escalabilidad**: Diseño permite agregar nuevos recursos/acciones sin refactoring masivo
5. **Pragmatismo**: 80% RBAC simple, 20% ABAC complejo solo donde se necesita

Las reglas garantizan que el sistema sea evaluable, trazable y listo para exhibición profesional en portfolio internacional.