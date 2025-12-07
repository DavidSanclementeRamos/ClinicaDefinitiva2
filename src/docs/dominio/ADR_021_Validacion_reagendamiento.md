# ADR-17 (Dominio): Validaciones de Reagendamiento de Citas

- Estado: Aprobado
- Fecha: 2025-10-08
- Autor: David

## Contexto
En el dominio clínico, el reagendamiento de citas debe cumplir con múltiples reglas de negocio para garantizar:
- Legitimidad clínica: no reagendar citas pasadas o en curso.
- Coherencia de actores: paciente y odontólogo correctos y activos.
- Disponibilidad real de la agenda: sin conflictos, dentro de horarios laborales.
- Políticas de la clínica: anticipación mínima, ventana máxima, capacidad diaria.

Actualmente existen métodos auxiliares en Schedule (findAppointmentsWithinHours, findAppointmentsOn, findAppointmentsWithin, getTotalOccupiedTime, getTotalAvailableTime) que permiten expresar estas reglas de forma consistente y exhibible.

Decisión
Las validaciones de reagendamiento se distribuyen de la siguiente manera:

## Appointment
- Verifica que la cita esté en estado Scheduled.
- Verifica que la cita sea vigente (no pasada ni en curso).
- Verifica identidad: mismo paciente y mismo odontólogo.

## Patient
- Valida que el paciente esté activo.

## Dentist
- Valida que el odontólogo esté activo.
- Valida que el nuevo horario esté dentro de su jornada laboral (canWorkBetween → WorkingHours.isWithinRange).

## Schedule
- Valida disponibilidad y conflictos (canScheduleBetween).
- Aplica políticas temporales:
  - Tiempo mínimo de anticipación: findAppointmentsWithinHours(...).
  - Ventana máxima de reagendamiento: findAppointmentsWithin(...).
- Opcionalmente:
  - Capacidad diaria: findAppointmentsOn(date).
  - Carga de trabajo total: getTotalOccupiedTime() y getTotalAvailableTime(day).

## Justificación
- Alta cohesión: cada agregado valida lo que le corresponde.
- Reutilización: se aprovechan queries y cálculos de Schedule.
- Exhibible: cada regla queda trazada a un método concreto.
- Evolutivo: cambios en políticas se ajustan en constantes o queries.

## Consecuencias
- Se asegura consistencia clínica y administrativa.
- Se evita duplicación de lógica.
- Se facilita auditoría y documentación.
- Se habilita evolución futura sin romper el modelo.

## Plan de implementación
1. Crear AppointmentRescheduleService en com.clinica.domain.service.
2. Implementar validaciones distribuidas según agregados.
3. Refactorizar Schedule para centralizar políticas de anticipación y ventana máxima.
4. Documentar reglas en docs/dominio/reglas-de-negocio/reagendamiento.md.
5. Añadir pruebas unitarias para escenarios:
  - Cita pasada → excepción.
  - Paciente inactivo → excepción.
  - Odontólogo fuera de jornada → excepción.
  - Conflicto de agenda → excepción.
  - Políticas de anticipación/ventana → excepción.

## Ejemplo
```java
public class AppointmentRescheduleService {
public void reschedule(Appointment appointment, LocalDateTime newStart, LocalDateTime newEnd) {
appointment.validateScheduledAndActive();
appointment.validateIdentity();

        appointment.getPatient().validateActive();
        appointment.getDentist().validateActive();
        appointment.getDentist().canWorkBetween(newStart, newEnd);

        appointment.getSchedule().canScheduleBetween(newStart, newEnd);
        appointment.getSchedule().validatePolicies(newStart, newEnd);
    }
}
```

## Checklist de Validaciones
- [x] Estado: cita programada y vigente.
- [x] Identidad: paciente y odontólogo correctos.
- [x] Actividad: paciente y odontólogo activos.
- [x] Agenda: disponibilidad y sin conflictos.
- [x] Políticas: anticipación mínima y ventana máxima.
- [ ] Opcionales: capacidad diaria y carga de trabajo total.

## Relación con otros ADR
- ADR-13 (Dominio): Mantener y refinar upcomingWithinHours como consulta de soporte.
- ADR-12 (Dominio): Eliminación del método upcomingWithinHours(int hours).
- ADR-08 (Dominio): Refactorización semántica con canScheduleAt(...).  
  

