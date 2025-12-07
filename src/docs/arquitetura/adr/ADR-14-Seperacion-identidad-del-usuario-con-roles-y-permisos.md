# ADR-14 (Arquitectura): Separación de Identity y Administración

- Estado: Aprobado
- Fecha: 2025-11-17
- Autor: David

## Contexto
El sistema actual mezcla en el módulo Identity tanto la identidad técnica del usuario (credenciales, estados de seguridad) como los roles y permisos de negocio.  
Esto dificulta la claridad semántica y la exhibición de reglas administrativas reales.

## Decisión
Se separa la responsabilidad en dos módulos:

## Identity
- Contiene UserIdentity con credenciales, estados técnicos y auditoría.
- No incluye roles ni permisos de negocio.
- Su responsabilidad es únicamente la autenticación y validación técnica.

## Administración
- Contiene Role, UserRoleAssignment, PermissionPolicy y AuthorizationService.
- Define roles organizativos (DENTIST, GUARDIAN, PATIENT, RECEPTIONIST).
- Implementa reglas de negocio explícitas para permisos CRUD (SAVE, UPDATE, DELETE, READ).
- Evalúa permisos en función de rol vigente y contexto.

## Consecuencias
Positivas
- Claridad semántica: separación entre identidad técnica y reglas de negocio.
- Flexibilidad: cambios en roles y permisos no afectan autenticación.
- Exhibición: el módulo administrativo refleja complejidad organizativa real.
- Auditabilidad: decisiones de autorización pueden registrarse con rol, permiso y contexto.

Negativas
- Mayor complejidad inicial en la arquitectura.
- Necesidad de sincronizar datos entre módulos (userId como referencia).
- Requiere documentación y formación adicional para el equipo.

## Plan de implementación
1. Crear módulo Identity con entidad UserIdentity y servicios de autenticación.
2. Crear módulo Administración con entidades Role, UserRoleAssignment, PermissionPolicy.
3. Implementar AuthorizationService que evalúe permisos según rol y contexto.
4. Sincronizar userId como referencia entre módulos.
5. Documentar separación en docs/arquitectura/identity-vs-administration.md.
6. Añadir pruebas unitarias para autenticación y autorización por separado.

## Ejemplo
```java
// Identity
public final class UserIdentity {
private final String userId;
private final String username;
private final String passwordHash;
private final boolean active;
}

// Administración
public final class UserRoleAssignment {
private final String userId;
private final Role role;
}
```

## Relación con otros ADR
- [ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización.](ADR-15-Revocacion-de-permisos.md)
- [ADR-16 (Arquitectura): Permisos de menú en el sistema.](ADR-16-Permisos-de-menu.md)
- [ADR-13 (Arquitectura): Separar DTOs por operación y DTOs de Update por tipo de datos.](ADR-13-DTO-por-operaciones-y-updateDto-por-tipos-de-datos.md) 
  

