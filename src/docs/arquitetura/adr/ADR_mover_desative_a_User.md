# ADR: Desactivación de usuario debe ser responsabilidad del agregado UserModel

## Estado
Propuesto

## Contexto
Actualmente, algunos agregados como `Guardian` exponen métodos que permiten desactivar el usuario asociado (`UserModel`). Esta lógica implica modificar directamente el estado del usuario desde agregados dependientes, lo cual rompe el principio de encapsulamiento y puede generar inconsistencias en el dominio.

En el diseño actual, `UserModel` representa la identidad y el estado activo/inactivo del usuario. Por tanto, es el único agregado que debería tener la autoridad para modificar su propio estado. Los agregados que dependen de `UserModel` (como `Guardian`, `Dentist`, etc.) deben limitarse a validar si existen condiciones que impidan la desactivación, pero no ejecutar dicha acción.

## Decisión
La operación de desactivar un usuario será movida al agregado `UserModel`. Este será el único responsable de cambiar su estado (`ACTIVE` → `INACTIVE`), asegurando que las reglas de negocio internas se respeten y que el cambio se realice de forma coherente.

Los agregados dependientes como `Guardian` solo expondrán métodos como `canDeactivateUser()` o `validateDeactivationAllowed()` que permitan verificar si existen restricciones (por ejemplo, pacientes asignados) que impidan la desactivación.

## Consecuencias
- Se mejora la encapsulación y claridad de responsabilidades en el modelo de dominio.
- Se evita que agregados secundarios muten el estado de entidades centrales como `UserModel`.
- Se facilita la trazabilidad de cambios de estado del usuario.
- Se requiere refactorizar los métodos actuales en `Guardian`, `Dentist`, etc., para que no modifiquen directamente el estado del usuario.

## Nota de implementación
Esta modificación se realizará en su debido momento, cuando se aborde el desarrollo o refactorización del módulo `identity`, que es donde reside el agregado `UserModel`. Hasta entonces, se mantendrá la lógica actual como solución temporal, documentando esta decisión para asegurar su correcta implementación futura.

## Autor
David

## Fecha
2025-11-12