# ADR-1 (Dominio): Desactivación de usuario debe ser responsabilidad del agregado UserModel

- Estado: Propuesto
- Fecha: 2025-11-12
- Autor: David

## Contexto
Actualmente, algunos agregados como Guardian exponen métodos que permiten desactivar el usuario asociado (UserModel).  
Esto rompe el principio de encapsulamiento y puede generar inconsistencias en el dominio, ya que un agregado dependiente no debería mutar directamente el estado de otro agregado central.

En el diseño actual, UserModel representa la identidad y el estado activo/inactivo del usuario.  
Por tanto, es el único agregado que debería tener la autoridad para modificar su propio estado.  
Los agregados dependientes (Guardian, Dentist, etc.) deben limitarse a validar condiciones que impidan la desactivación, pero no ejecutar la acción.

## Decisión
La operación de desactivar un usuario será movida al agregado UserModel.  
Este será el único responsable de cambiar su estado (ACTIVE → INACTIVE), asegurando que las reglas internas se respeten y que el cambio se realice de forma coherente.

Los agregados dependientes como Guardian solo expondrán métodos como canDeactivateUser() o validateDeactivationAllowed() para verificar restricciones (ej. pacientes asignados).

## Consecuencias
- Se mejora la encapsulación y claridad de responsabilidades en el modelo de dominio.
- Se evita que agregados secundarios muten el estado de entidades centrales como UserModel.
- Se facilita la trazabilidad de cambios de estado del usuario.
- Se requiere refactorizar los métodos actuales en Guardian, Dentist, etc., para que no modifiquen directamente el estado del usuario.

## Nota de implementación
Esta modificación se realizará en su debido momento, cuando se aborde el desarrollo o refactorización del módulo identity, donde reside el agregado UserModel.  
Hasta entonces, se mantendrá la lógica actual como solución temporal, documentando esta decisión para asegurar su correcta implementación futura.

## Plan de implementación
1. Crear método deactivate() en UserModel que encapsule la lógica de cambio de estado.
2. Refactorizar Guardian y Dentist para exponer solo validaciones (canDeactivateUser()).
3. Actualizar servicios de aplicación para invocar UserModel.deactivate() en lugar de métodos externos.
4. Documentar reglas en docs/dominio/reglas-de-negocio/identity.md.
5. Añadir pruebas unitarias:
    - Usuario activo → desactivación válida.
    - Usuario con restricciones → excepción.

## Ejemplo
```java
// En UserIdentity
public void deactivate() {
if (!this.isActive()) {
throw new UserAlreadyInactiveException(this.id);
}
this.state = UserState.INACTIVE;
}

// En Guardian
public boolean canDeactivateUser() {
return this.patients.isEmpty();
}
```

## Relación con otros ADR
- ADR-15 (Dominio): Validación de Guardian en el agregado Patient.
- ADR-11 (Dominio): Uso meticuloso de excepciones personalizadas.
- ADR-20 (Dominio): Inquietud sobre el rol de los Servicios de Dominio frente a métodos en Agregados.  
  