# ADR-38 (Arquitectura): Ubicación de validaciones de desactivación

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Arquitectura

## Problema

La desactivación de usuarios requiere validaciones en distintos agregados. No está claro si estas validaciones deben vivir en los agregados o en Domain Services especializados.

**Ejemplo:**
- `Guardian` tiene lista de pacientes asignados: ¿validación interna?
- `Dentist` debe verificar citas en las próximas 24h: ¿requiere Schedule?

## Decisión

Aplicar la siguiente regla de localización de validaciones:

### Regla de decisión

| Condición | Ubicación | Ejemplo |
|-----------|-----------|---------|
| Validación depende **solo de atributos internos** del agregado | Método en el agregado | `Guardian.validateDeactivation()` verifica si `patientList.isEmpty()` |
| Validación requiere **coordinar con otro agregado** o repositorio | Domain Service especializado | `DentistDeactivationValidator` consulta `Schedule` para verificar citas |
| Validación involucra **múltiples agregados** | Orquestador (Policy) | `UserDeactivationPolicy` invoca múltiples validadores |

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Todas las validaciones en agregados | Requeriría inyectar repositorios en agregados (anti-patrón) |
| Todas las validaciones en services | Agregados pierden cohesión, validaciones triviales quedan fuera |
| Todas las validaciones en Application Service | Lógica de negocio en capa de aplicación |

## Consecuencias

**Ganamos:**
- Regla clara para decidir dónde ubicar cada validación
- Agregados mantienen cohesión en reglas simples
- Domain Services manejan coordinación compleja

**Perdemos:**
- Decisión case-by-case (requiere análisis por cada validación)
- Posible inconsistencia si la regla no se documenta

## Implementación

```java
// Validación INTERNA en agregado
public class Guardian {
    private List<PatientId> patientList;
    
    public Outcome<Void> validateDeactivation() {
        if (!patientList.isEmpty()) {
            return Outcome.fail(
                ErrorCatalogXD.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS
            );
        }
        return Outcome.ok();
    }
}

// Validación COORDINADA en Domain Service
public class DentistDeactivationValidator {
    private final ScheduleRepository scheduleRepo;
    
    public Outcome<Void> validate(DentistId dentistId) {
        Schedule schedule = scheduleRepo.findByDentistId(dentistId);
        if (schedule.hasAppointmentsWithinHours(24)) {
            return Outcome.fail(
                ErrorCatalogXD.ERR_DENTIST_HAS_PENDING_APPOINTMENTS
            );
        }
        return Outcome.ok();
    }
}
```