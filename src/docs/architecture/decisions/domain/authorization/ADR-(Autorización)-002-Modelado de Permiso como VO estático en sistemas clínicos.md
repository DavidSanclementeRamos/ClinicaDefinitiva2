

# ADR-002 (Autorización): Modelado de Permiso como VO estático en sistemas clínicos

- **Fecha**: 2026-02-02
- **Estado**: Aprobado
- **Categoría**: Seguridad / Dominio
- **Complementa**: ADR-47 (Modelo híbrido RBAC/ABAC)
- **Autor:** David Stiven Sanclemente

---

## Contexto

El sistema clínico maneja información sensible de pacientes y odontólogos.  
Se requiere un modelo de autorización que sea seguro, auditable y mantenible.

Durante el diseño se evaluó si `Permission` debía ser:
- **VO estático** (lista cerrada en código, embebida en roles).
- **Entidad persistida** (tabla `permisos`, configurable en runtime).

---

## Problema

- Los permisos configurables ofrecen flexibilidad, pero introducen riesgos:
    - Un administrador podría crear permisos inseguros.
    - Mayor complejidad en repositorios, mappers y políticas.
    - Dificultad para auditar y certificar en un entorno regulado.

- Los permisos estáticos reducen flexibilidad, pero:
    - Garantizan seguridad y trazabilidad.
    - Simplifican la arquitectura.
    - Se alinean con normativas de salud (ej. HIPAA, Ley 1581).

---

## Decisión

Modelar `Permission` como **Value Object estático** en el dominio.
- Los permisos se definen en catálogos (`Resources`, `Actions`).
- Se combinan en `RoleBasedPolicy` y políticas ABAC específicas.
- Los **roles sí son configurables**, pero siempre asignando permisos de un catálogo cerrado.

---

## Consecuencias

 **Seguridad regulatoria**: no se pueden crear permisos arbitrarios en runtime.  
 **Auditoría clara**: lista fija de permisos, fácil de certificar.  
 **Simplicidad**: menos complejidad en repositorios y mappers.  
 **Menos flexibilidad**: agregar un nuevo permiso requiere despliegue de código.  
 **Mitigación**: roles configurables permiten adaptar combinaciones sin tocar permisos base.

---

## Alternativas Consideradas

1. **Permiso como Entidad persistida**
    - Rechazado por riesgo de crear permisos inseguros y complejidad innecesaria en un sistema regulado.

2. **Permisos embebidos por recurso (granular)**
    - Rechazado por explosión de clases y mantenimiento pesado (cubierto en ADR-38).

---

## Roles Configurables vs Permisos Estáticos

### Permisos (Estáticos):
- Catálogo cerrado definido en código
- ResourceCatalog.BasicResource + ActionCatalog.BasicAction
- NO se pueden crear permisos custom en runtime
- Auditable y certificable

### Roles (Configurables):
- Roles BASE: Predefinidos, no editables (DENTIST, PATIENT, etc.)
- Roles CLONADOS: Derivados de roles base, editables
- Solo pueden tener permisos del catálogo estático
- Permite adaptación a necesidades organizativas

### Ejemplo:

Rol base DENTIST tiene permisos:
- CREATE_TREATMENT
- READ_TREATMENT
- UPDATE_TREATMENT
- DELETE_TREATMENT

Rol clonado "Dentista Junior" (subconjunto):
- READ_TREATMENT
- UPDATE_TREATMENT

Rol clonado "Dentista Senior" (superset):
- [todos de DENTIST]
- CREATE_DENTIST (mentor)

 Todos los permisos vienen del catálogo estático
 No se pueden crear permisos custom como "APPROVE_EXPERIMENTAL_PROCEDURE"

---

## Relación con otros ADRs

- **Complementa ADR-38**: define que el modelo híbrido RBAC/ABAC se basa en permisos estáticos.
- **Complementa ADR-37**: mantiene separación hexagonal (Permission como VO en dominio).
- **Complementa ADR-46**: tokens JWT incluyen roles, no permisos dinámicos.

