# ADR-16 (Arquitectura): Permisos de menú en el sistema

- Estado: Aprobado (pendiente de implementación)
- Fecha: 2025-11-20
- Autor: David

## Contexto
El sistema de autorización se basa en roles predefinidos y atributos dinámicos.  
Surge la necesidad de controlar no solo las acciones de backend, sino también la visibilidad de menús en la interfaz.  
En algunos cursos se propone modelar los menús como clases con permisos asociados. La duda era si esto es un capricho o una práctica común.

## Opciones consideradas
1. Condicionales en frontend
    - Mostrar/ocultar menús según el rol del usuario directamente en la UI.
    - Simple, pero dispersa la lógica de autorización.

2. Permisos de menú como parte del dominio
    - Modelar MenuItem como entidad/VO con permisos asociados.
    - Persistir en BD o catálogo.
    - El frontend solo consume qué menús mostrar, sin lógica propia.

## Decisión
Se decide modelar los menús como parte del dominio.  
La justificación es que esto mantiene la lógica de autorización centralizada, evita duplicación en el frontend y exhibe madurez arquitectónica.

## Consecuencias
Positivas
- Consistencia entre backend y frontend.
- Escalabilidad: menús dinámicos según roles/permisos.
- Exhibición clara de que la UI está gobernada por reglas de negocio.

Negativas
- Mayor complejidad inicial.
- Requiere diseño previo antes de tener frontend.

Catálogo de menús con permisos
| MenuItem ID | Label              | Path             | Permisos requeridos         | Roles comunes visibles |
|-------------|--------------------|------------------|-----------------------------|------------------------|
| M01         | Gestión de usuarios| /admin/users     | crearusuario, verusuario    | ADMIN                  |
| M02         | Reportes           | /reports         | ver_reportes                | ADMIN, GESTOR          |
| M03         | Mi perfil          | /profile         | verperfil, editarperfil     | USER, PACIENTE         |
| M04         | Facturación        | /billing         | verfacturas, crearfactura   | ADMIN, CONTADOR        |

## Plan de implementación
1. Definir entidad/VO MenuItem con atributos: id, label, path, permisos requeridos, roles visibles.
2. Persistir catálogo de menús en BD o archivo de configuración inicial.
3. Integrar MenuItem con el sistema de autorización (roles + permisos).
4. Exponer endpoint GET /menus que devuelva menús visibles para el usuario autenticado.
5. Ajustar frontend para consumir el catálogo y renderizar menús dinámicamente.
6. Documentar en docs/arquitectura/menus.md la relación entre menús y permisos.

## Ejemplo
```java
public class MenuItem {
private final String id;
private final String label;
private final String path;
private final Set<String> requiredPermissions;

    public boolean isVisibleFor(User user) {
        return user.hasAnyPermission(requiredPermissions);
    }
}
```

## Relación con otros ADR
- [ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización.](ADR-15-Revocacion-de-permisos.md)
- [ADR-07 (Arquitectura): Redefinición del módulo Administration.](ADR-07-Redefinición%20del%20módulo%20Administration.md)
- [ADR-13 (Arquitectura): Separar DTOs por operación y DTOs de Update por tipo de datos.](ADR-13-DTO-por-operaciones-y-updateDto-por-tipos-de-datos.md)  
  

