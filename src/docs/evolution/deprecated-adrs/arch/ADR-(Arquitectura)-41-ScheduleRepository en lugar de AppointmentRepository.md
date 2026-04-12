# ADR-41 (Arquitectura): ScheduleRepository en lugar de AppointmentRepository

- **Fecha**: 2026-01-30
- **Estado:** Superado por [ADR-(Cita)-08](../../../architecture/decisions/domain/schedule/ADR-(Cita)-08-Transformación%20de%20Schedule%20en%20ScheduleQueryService.md)
- **Categoría**: Arquitectura
- **Autor:** David Stiven Sanclemente
---
> **Nota histórica:** Este ADR proponía tratar `Schedule` como agregado raíz con repositorio propio.  
> La decisión final (ADR-08 Cita) eliminó `Schedule` como agregado y lo transformó en `ScheduleQueryService`, un servicio de consulta sin estado.  
> La implementación actual sigue ADR-08.
---
## Problema

El agregado `Schedule` coordina `Appointment` y `Availability`. Expone queries semánticas como `hasAppointmentsWithinHours()` que encapsulan lógica de negocio.

No está claro si debemos consultar directamente un `AppointmentRepository` o usar `ScheduleRepository` para obtener toda la agenda.

## Decisión

Usar **`ScheduleRepository`** como punto de acceso único a la agenda de un dentista.

**Regla:** Siempre que la lógica de negocio involucre tanto citas como disponibilidad, obtener el `Schedule` completo desde infraestructura.

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Consultar `AppointmentRepository` directamente | Rompe la cohesión del agregado. Las queries semánticas viven en `Schedule`, no en colecciones de `Appointment` |
| Tener ambos repositorios | Duplicación. `Schedule` ya contiene las citas |
| Exponer `Appointment` como agregado raíz | `Appointment` no tiene sentido sin contexto de `Schedule` (ej. disponibilidad) |

## Consecuencias

**Ganamos:**
- Cohesión del modelo: `Schedule` es el agregado raíz
- Queries semánticas trazables (`hasAppointmentsWithinHours` está en el dominio, no en SQL)
- Validaciones centralizadas en el agregado

**Perdemos:**
- Queries más pesadas: reconstruir `Schedule` completo es más costoso que consultar solo citas
- No optimizado para reportes analíticos (ej. "todas las citas del mes")

## Excepción para reportes

Para queries analíticas puras (reportes, estadísticas) donde NO se requiere lógica de negocio, sí puede existir un **read model** separado:

```java
// Para lógica de negocio: Schedule completo
Schedule schedule = scheduleRepository.findByDentistId(dentistId);
if (schedule.hasAppointmentsWithinHours(24)) { ... }

// Para reportes: read model directo
List<AppointmentSummary> report = appointmentQueryService.findAllByMonth(month);
```

## Implementación

```java
// Puerto de salida
public interface ScheduleRepository {
    Optional<Schedule> findByDentistId(DentistId dentistId);
    void save(Schedule schedule);
}

// Application Service
public void deactivateDentist(DentistId dentistId) {
    Schedule schedule = scheduleRepository.findByDentistId(dentistId)
        .orElseThrow();
    
    // Query semántica en el agregado
    if (schedule.hasAppointmentsWithinHours(24)) {
        throw new BusinessRuleViolationException(
            ErrorCatalogXD.ERR_DENTIST_HAS_PENDING_APPOINTMENTS
        );
    }
}
```