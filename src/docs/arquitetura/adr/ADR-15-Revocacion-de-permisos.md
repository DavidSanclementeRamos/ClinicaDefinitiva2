## ADR-15 (Arquitectura): Revocación de permisos en el sistema de autorización

- Estado: Aprobado
- Fecha: 2025-11-20
- Autor: David

## Contexto
El sistema de autorización se basa en un modelo híbrido:
- Roles predefinidos que cubren el 80% de los permisos comunes.
- Atributos dinámicos que refinan el acceso en un 20% de casos específicos.

Durante el diseño surgió la duda de si implementar la capacidad de revocar permisos (quitar un permiso que normalmente vendría dado por el rol).

## Opciones consideradas
1. No implementar revocación
    - Mantener simplicidad: roles + atributos cubren la mayoría de casos.
    - Evitar complejidad adicional en la lógica de autorización.
    - Los permisos adicionales se pueden otorgar caso por caso, pero no se quitan.

2. Implementar revocación explícita
    - Permitir excepciones: sanciones, conflictos de interés, restricciones legales.
    - Añadir una lista de “permisos denegados” evaluada después de roles y atributos.
    - Aumenta flexibilidad, pero también complejidad y riesgo de inconsistencias.

## Decisión
Se decide no implementar revocación en la primera versión del sistema.  
La justificación es que los casos de negocio que requieren revocación son poco frecuentes y el costo de complejidad supera el beneficio inmediato.

Sin embargo, el diseño se deja abierto a extensión futura:
- La lógica de autorización puede incorporar fácilmente una lista de permisos denegados si surge la necesidad.
- Esto permite evolucionar el sistema sin romper el modelo actual.

## Consecuencias
Positivas
- Simplicidad en la implementación inicial.
- Claridad en la documentación y exhibición del modelo híbrido.
- Menor riesgo de inconsistencias.

## Negativas
- No se cubren casos raros donde un rol debe perder un permiso.
- Si surge esa necesidad, habrá que extender el sistema con una capa de revocación.

## Plan de implementación
1. Implementar autorización inicial basada en roles + atributos.
2. Documentar explícitamente que la revocación no está soportada en la primera versión.
3. Diseñar interfaz extensible para incluir lista de permisos denegados en el futuro.
4. Añadir pruebas unitarias para roles y atributos, dejando espacio para pruebas de revocación futura.

## Ejemplo
```java
// Evaluación actual: roles + atributos
boolean hasPermission = role.hasPermission("FACTURAR") || attributes.contains("FACTURAR");

// Extensión futura: lista de permisos denegados
boolean hasPermission = (role.hasPermission("FACTURAR") || attributes.contains("FACTURAR"))
&& !deniedPermissions.contains("FACTURAR");
```

## Relación con otros ADR
- [ADR-07 (Arquitectura): Redefinición del módulo Administration (roles administrativos).](ADR-07-Redefinición%20del%20módulo%20Administration.md)
- [ADR-13 (Arquitectura): Separar DTOs por operación y DTOs de Update por tipo de datos (seguridad de datos).](ADR-13-DTO-por-operaciones-y-updateDto-por-tipos-de-datos.md)  
  

