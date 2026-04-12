
# ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización

- Estado: Aprobado (Revisado 2026-04-2)
- Fecha original: 2025-11-20
- Última revisión: 2026-04-2
- Autor: David Stiven Sanclemente

## Contexto
El sistema de autorización se basa en un modelo híbrido RBAC/ABAC con permisos estáticos (ADR-002).

Durante el diseño original se evaluó si implementar revocación de permisos. La decisión inicial fue no hacerlo por simplicidad.

Sin embargo, tras la refactorización del módulo de autorización, el sistema evolucionó y ahora soporta revocación en roles clonados.

## Decisión REVISADA (2026-03-29)

### Permitir revocación en roles CLONADOS únicamente:

1. **Roles base (isEditable=false):**
   - NO se puede revocar permisos
   - Permisos fijos definidos en código (RoleBasedPolicy)
   - Ejemplo: DENTIST siempre tiene CREATE_TREATMENT

2. **Roles clonados (isEditable=true):**
   - SÍ se puede revocar permisos
   - Solo permisos del catálogo estático (ADR-002)
   - Ejemplo: "Dentista Junior" = DENTIST sin CREATE_TREATMENT

### Reglas de Revocación:
```java
//  PERMITIDO: Revocar en rol clonado
Rol dentistJunior = Rol.cloneFrom(dentistBase, "Dentista Junior");
dentistJunior.removePermission(Permission.create(TREATMENT)); // OK

//  PROHIBIDO: Revocar en rol base
Rol dentist = Rol.createDefault(RolEnum.DENTIST, "Dentista");
dentist.removePermission(Permission.create(TREATMENT)); // ERROR (isEditable=false)
```

### NO permitir:
- Crear permisos nuevos en runtime (catálogo cerrado)
- Modificar roles base del sistema
- Revocar permisos sin validación de negocio

## Consecuencias

**Positivas:**
- Flexibilidad para roles personalizados (clonados)
- Seguridad mantenida (catálogo cerrado de permisos)
- Trazabilidad: roles clonados son auditables
- Los roles base permanecen inmutables y certificables

**Negativas:**
- Complejidad adicional (mínima, ya implementada)
- La decisión original fue superada por la evolución

## Implementación Actual

El código en `Rol.java` es CORRECTO:
```java
public void removePermission(Permission permission) {
    ensureEditable();  // ← Valida isEditable=true
    if(this.permissions.isEmpty()){
        throw new BusinessRuleViolationException(...);
    }
    this.permissions.remove(permission);
}
```
 - Solo roles clonados (isEditable=true) pueden revocar
 -  No se pueden crear permisos custom (catálogo cerrado)
 -  Roles base permanecen inmutables

## Relación con otros ADR
- [ADR-(Arquitectura)-07-Redefinición del módulo Administration.md](ADR-%28Arquitectura%29-07-Redefinici%C3%B3n%20del%20m%C3%B3dulo%20Administration.md)
- [ADR-(Arquitectura)-38-UserDeactivationPolicy como orquestador de validaciones.md](ADR-%28Arquitectura%29-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md)

