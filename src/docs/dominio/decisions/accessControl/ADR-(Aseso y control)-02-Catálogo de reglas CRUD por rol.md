# ADR-30 (Dominio): Catálogo de reglas CRUD por rol

- Estado: Aprobado
- Fecha: 2025-11-17
- Autor: David

## Contexto
Los roles definidos actualmente son: DENTIST, GUARDIAN, PATIENT, RECEPTIONIST.  
Los permisos disponibles son genéricos: SAVE, UPDATE, DELETE, READ.  
Este catálogo establece qué operaciones puede realizar cada rol sobre los agregados del sistema.

## Reglas por rol

DENTIST
- SAVE: Puede registrar tratamientos y notas clínicas.
- UPDATE: Puede actualizar información de pacientes bajo su atención.
- DELETE: No permitido (no puede eliminar pacientes ni colegas).
- READ: Puede consultar historiales clínicos de sus pacientes.

GUARDIAN
- SAVE: Puede registrar información de contacto y autorizaciones.
- UPDATE: Puede actualizar datos propios y de pacientes bajo su tutela.
- DELETE: No permitido.
- READ: Puede consultar información administrativa de pacientes bajo su tutela.

PATIENT
- SAVE: Puede registrar información personal inicial (auto-registro).
- UPDATE: Puede actualizar sus propios datos personales.
- DELETE: No permitido.
- READ: Puede consultar su propio historial clínico y administrativo.

RECEPTIONIST
- SAVE: Puede registrar nuevos pacientes y odontólogos en el sistema.
- UPDATE: Puede actualizar datos administrativos (agenda, contacto).
- DELETE: Puede eliminar registros administrativos (ej. cancelar citas, dar de baja odontólogos).
- READ: Puede consultar información general de pacientes y odontólogos.

## Consecuencias
- Claridad en la asignación de responsabilidades.
- Exhibición de reglas organizativas reales.
- Base para implementar PermissionPolicy en el módulo de Administración.

## Plan de implementación
1. Definir PermissionPolicy por rol en el módulo Administración.
2. Implementar AuthorizationService que evalúe permisos CRUD según rol y contexto.
3. Documentar catálogo en docs/dominio/permissions.md.
4. Añadir pruebas unitarias para cada rol y operación CRUD.
5. Validar reglas con expertos administrativos y clínicos.

## Ejemplo
```java
PermissionPolicy dentistPolicy = PermissionPolicy.forRole("DENTIST")
.allow("SAVE", "Treatment")
.allow("UPDATE", "Patient")
.deny("DELETE", "Patient")
.allow("READ", "ClinicalHistory");

boolean canUpdate = dentistPolicy.isAllowed("UPDATE", "Patient"); // true
boolean canDelete = dentistPolicy.isAllowed("DELETE", "Patient"); // false
```

## Relación con otros ADR
- ADR-17 (Arquitectura): Separación de Identity y Administración.
- ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización.
- ADR-16 (Arquitectura): Permisos de menú en el sistema.
- ADR-29 (Dominio): Temas de estudio para comprender el modelo administrativo.  
  

