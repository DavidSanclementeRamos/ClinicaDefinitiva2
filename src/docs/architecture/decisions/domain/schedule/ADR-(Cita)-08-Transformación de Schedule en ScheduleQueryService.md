# ADR-08 (Cita): Transformación de Schedule en ScheduleQueryService

- **Estado:** Aprobado
- **Fecha:** 2026-02-07
- **Reemplaza:** [ADR-(Cita)-01-Motivos creación clase Schedule.md](../../../../evolution/deprecated-adrs/domain/ADR-%28Cita%29-01-Motivos%20creaci%C3%B3n%20clase%20Schedule.md) y [ADR-(Arquitectura)-41-ScheduleRepository en lugar de AppointmentRepository.md](../../../../evolution/deprecated-adrs/arch/ADR-%28Arquitectura%29-41-ScheduleRepository%20en%20lugar%20de%20AppointmentRepository.md)
- **Autor:** David Stiven Sanclemente


## Contexto
La clase Schedule actual:
```java
public final class Schedule {
    private final List<Appointment> appointments;
    private final Availability availability;
    
    public Duration getTotalOccupiedTime() { ... }
    public List<Appointment> findAppointmentsWithinHours(int hours) { ... }
}
```

**Problemas identificados:**
- No tiene identidad (no hay ScheduleId)
- No protege invariantes
- Solo expone queries
- No es un agregado DDD legítimo

## Decisión
Eliminar Schedule como clase de dominio y crear **ScheduleQueryService**.

**Antes:**
```java
Schedule schedule = new Schedule(appointments, availability);
boolean hasConflicts = schedule.hasAppointmentsWithinHours(24);
```

**Después:**
```java
@Service
public class ScheduleQueryService {
    private final AppointmentRepository appointmentRepository;
    
    public List<Appointment> findAppointmentsWithinHours(
        DentistId dentist, 
        int hours
    ) {
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findByDentistBetween(
            dentist, 
            now, 
            now.plusHours(hours)
        );
    }
    
    public Duration calculateOccupiedTime(DentistId dentist, LocalDate date) {
        return appointmentRepository.findByDentistAndDate(dentist, date)
            .stream()
            .map(a -> Duration.between(a.getStart(), a.getEnd()))
            .reduce(Duration.ZERO, Duration::plus);
    }
}
```

## Justificación
**Por qué Schedule NO es un agregado:**
-  No tiene identidad única
-  No protege invariantes de negocio
-  No tiene ciclo de vida propio
-  No se persiste

**Por qué ScheduleQueryService SÍ es correcto:**
-  Centraliza queries complejas
-  Delega en repositorio (separación de capas)
-  Reutilizable desde múltiples servicios
-  Testeable sin estado

## Consecuencias

**Positivas:**
- Arquitectura más clara (Service Layer explícito)
- Queries centralizadas en un solo lugar
- Fácil mockear en tests
- Sin estado compartido (thread-safe)

**Negativas:**
- Requiere refactorizar llamadas a Schedule
- Los métodos ahora requieren DentistId explícito



