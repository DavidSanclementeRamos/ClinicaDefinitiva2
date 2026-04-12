# ADR-38 (Arquitectura): UserDeactivationPolicy como orquestador de validaciones

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Arquitectura
- **Autor:** David Stiven Sanclemente
- **Reemplaza a:**
- [ADR-(Actores)-14-Eliminación de Domain Services en agregados del módulo Actor.md](../../../evolution/deprecated-adrs/domain/ADR-%28Actores%29-14-Eliminaci%C3%B3n%20de%20Domain%20Services%20en%20agregados%20del%20m%C3%B3dulo%20Actor.md)
- [ADR-(Actores)-15-Estrategia de desactivación de Usuarios y Actores.md](../../../evolution/deprecated-adrs/domain/ADR-%28Actores%29-15-Estrategia%20de%20desactivaci%C3%B3n%20de%20Usuarios%20y%20Actores.md)
## Problema

La desactivación de un usuario involucra validar múltiples agregados independientes:
- **Dentist**: sin citas pendientes
- **Patient**: sin tratamientos activos
- **Guardian**: sin responsabilidades activas
- **Receptionist**: sin turnos asignados

Si el Application Service invoca directamente cada Domain Service especializado, se acopla a las reglas de cada agregado y viola la separación de capas.

## Decisión

Introducir `UserDeactivationPolicy` como Domain Service orquestador que:
- Centraliza las validaciones de desactivación
- Delega en Domain Services especializados cuando corresponde
- Expone una única interfaz al Application Service

```java
// Application Service solo conoce la política
if (!userDeactivationPolicy.canDeactivate(user)) {
    throw new BusinessRuleViolationException(
        ErrorCatalogXD.ERR_USER_CANNOT_BE_DEACTIVATED
    );
}
```

## Criterio de decisión

**Usa orquestador cuando:**
- La validación involucra >2 agregados diferentes
- Cada agregado tiene su propio Domain Service especializado
- La lógica de coordinación es compleja

**No uses orquestador si:**
- Solo un agregado está involucrado → método en el agregado
- Dos agregados con lógica trivial → Application Service directo

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Application Service llama a cada validador | Acopla la capa de aplicación a reglas de dominio específicas |
| Método en agregado User | User no conoce las reglas de Dentist, Patient, etc. |
| Validadores en infraestructura | Reglas de negocio no pertenecen a infraestructura |

## Consecuencias

**Ganamos:**
- Application Service desacoplado de reglas específicas
- Punto único para agregar nuevos roles (ej. Hygienist)
- Trazabilidad clara de reglas de desactivación

**Perdemos:**
- Capa adicional de indirección
- Más clases en el dominio

## Implementación

```java
public class UserDeactivationPolicy {
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;
    // ... otros validadores
    
    public boolean canDeactivate(UserIdentity user) {
        // Delega según el rol del usuario
        switch(user.getRole()) {
            case DENTIST: return dentistValidator.canDeactivate(user.getId());
            case PATIENT: return patientValidator.canDeactivate(user.getId());
            // ...
        }
    }
}

```
## Relación con otros ADRs

- [ADR-(Arquitectura)-39-Ubicación de validaciones de desactivación.md](ADR-%28Arquitectura%29-39-Ubicaci%C3%B3n%20de%20validaciones%20de%20desactivaci%C3%B3n.md)