# ADR: Validaciones de Reagendamiento de Citas
## Estado
- Aceptado
- Fecha: 2025-10-8
## Contexto
En el dominio clínico, el reagendamiento de citas debe cumplir con múltiples reglas de negocio para garantizar:
- La legitimidad clínica (no reagendar citas pasadas o en curso).
- La coherencia de actores (paciente y odontólogo correctos y activos).
- La disponibilidad real de la agenda (sin conflictos, dentro de horarios laborales).
- Las políticas de la clínica (anticipación mínima, ventana máxima, capacidad diaria).

Actualmente existen métodos auxiliares en `Schedule` (`findAppointmentsWithinHours`, `findAppointmentsOn`, `findAppointmentsWithin`, `getTotalOccupiedTime`, `getTotalAvailableTime`) que permiten expresar estas reglas de forma consistente y exhibible.

## Decisión
Se define que las validaciones de reagendamiento se distribuyen de la siguiente manera:

### Appointment
- Verifica que la cita esté en estado **Scheduled**.
- Verifica que la cita sea **vigente** (no pasada ni en curso).
- Verifica identidad: mismo paciente y mismo odontólogo.

### Patient
- Valida que el paciente esté activo.

### Dentist
- Valida que el odontólogo esté activo.
- Valida que el nuevo horario esté dentro de su jornada laboral (`canWorkBetween` → `WorkingHours.isWithinRange`).

### Schedule
- Valida disponibilidad y conflictos (`canScheduleBetween`).
- Aplica políticas temporales:
    - **Tiempo mínimo de anticipación**: `findAppointmentsWithinHours(...)`.
    - **Ventana máxima de reagendamiento**: `findAppointmentsWithin(...)`.
- Opcionalmente:
    - **Capacidad diaria**: `findAppointmentsOn(date)`.
    - **Carga de trabajo total**: `getTotalOccupiedTime()` y `getTotalAvailableTime(day)`.

## Consecuencias
- **Alta cohesión**: cada agregado valida lo que le corresponde.
- **Reutilización**: se aprovechan queries y cálculos de `Schedule` en lugar de duplicar lógica en `Appointment`.
- **Exhibible**: cada regla de negocio queda trazada a un método concreto, facilitando auditoría y documentación.
- **Evolutivo**: si cambian las políticas (ej. anticipación mínima, ventana máxima), basta con ajustar constantes o queries, sin modificar la lógica central.

## Checklist de Validaciones
- [x] Estado: cita programada y vigente.
- [x] Identidad: paciente y odontólogo correctos.
- [x] Actividad: paciente y odontólogo activos.
- [x] Agenda: disponibilidad y sin conflictos.
- [x] Políticas: anticipación mínima y ventana máxima.
- [ ] Opcionales: capacidad diaria y carga de trabajo total.
