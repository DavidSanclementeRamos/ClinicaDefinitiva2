# Índice de ADRs — Catálogo de Errores Value Objects (Schedule)

### Appointment (Cita Clínica)
1. **ERR_APPOINTMENT_ID_REQUIRED**
    - Regla: RN-APPT-001
    - Descripción: El valor de AppointmentId no puede ser nulo.
    - [ScheduleVo-ERR_APPOINTMENT_ID_REQUIRED.md](ScheduleVo-ERR_APPOINTMENT_ID_REQUIRED.md)

2. **ERR_APPOINTMENT_ID_EMPTY**
    - Regla: RN-APPT-002
    - Descripción: El valor de AppointmentId no puede estar vacío.
    - [ScheduleVo-ERR_APPOINTMENT_ID_EMPTY.md](ScheduleVo-ERR_APPOINTMENT_ID_EMPTY.md)

3. **ERR_APPOINTMENT_STATUS_REQUIRED**
    - Regla: RN-APPT-001
    - Descripción: El estado de Appointment no puede ser nulo.
    - [ScheduleVo-ERR_APPOINTMENT_STATUS_REQUIRED.md](ScheduleVo-ERR_APPOINTMENT_STATUS_REQUIRED.md)

4. **ERR_APPOINTMENT_STATUS_INVALID_TRANSITION**
    - Regla: RN-APPT-002
    - Descripción: No se puede transicionar desde el estado actual a un estado inválido.
    - [ScheduleVo-ERR_APPOINTMENT_STATUS_INVALID_TRANSITION.md](ScheduleVo-ERR_APPOINTMENT_STATUS_INVALID_TRANSITION.md)

---