

# Índice de ADRs — Catálogo de Errores Appointment (Schedule)

### Creación y validaciones iniciales
1. **ERR_APPT_OUTSIDE_AVAILABILITY**
    - Regla: RN-APPT-002
    - Descripción: No puede agendarse fuera del horario de disponibilidad.
    - [ADR-XX-ERR_APPT_OUTSIDE_AVAILABILITY.md](ERR_APPT_OUTSIDE_AVAILABILITY.md)

2. **ERR_APPT_MISSING_REQUIRED_FIELDS**
    - Regla: RN-APPT-003
    - Descripción: Debe tener paciente y odontólogo válidos.
    - [ADR-XX-ERR_APPT_MISSING_REQUIRED_FIELDS.md](-ERR_APPT_MISSING_REQUIRED_FIELDS.md)

3. **ERR_APPT_DENTIST_TIME_CONFLICT**
    - Regla: RN-APPT-004
    - Descripción: No puede haber dos citas en el mismo horario para el mismo odontólogo.
    - [ADR-XX-ERR_APPT_DENTIST_TIME_CONFLICT.md](ERR_APPT_DENTIST_TIME_CONFLICT.md)

4. **ERR_APPT_PATIENT_TIME_CONFLICT**
    - Regla: RN-APPT-009
    - Descripción: No puede haber dos citas en el mismo horario para el mismo paciente.
    - [ADR-XX-ERR_APPT_PATIENT_TIME_CONFLICT.md](ERR_APPT_PATIENT_TIME_CONFLICT.md)

5. **ERR_APPT_PAST_DATE**
    - Regla: RN-APPT-010
    - Descripción: La fecha/hora de la cita no puede estar en el pasado.
    - [ADR-XX-ERR_APPT_PAST_DATE.md](ERR_APPT_PAST_DATE.md)

6. **ERR_APPT_MISSING_REASON**
    - Regla: RN-APPT-011
    - Descripción: Motivo clínico es obligatorio.
    - [ADR-XX-ERR_APPT_MISSING_REASON.md](ERR_APPT_MISSING_REASON.md)

---

### Finalización y edición
7. **ERR_APPT_INCOMPLETE_COMPLETION**
    - Regla: RN-APPT-005
    - Descripción: Solo puede finalizarse si tiene duración real y notas clínicas.
    - [ADR-XX-ERR_APPT_INCOMPLETE_COMPLETION.md](ERR_APPT_INCOMPLETE_COMPLETION.md)

8. **ERR_APPT_NOT_EDITABLE**
    - Regla: RN-APPT-006
    - Descripción: Solo puede editarse si está en estado SCHEDULED o CONFIRMED.
    - [ADR-XX-ERR_APPT_NOT_EDITABLE.md](ERR_APPT_NOT_EDITABLE.md)

---

### Cancelaciones y reagendamientos
9. **ERR_APPT_LATE_CANCELLATION**
    - Regla: RN-APPT-007
    - Descripción: No puede cancelarse dentro de las 24h previas.
    - [ADR-XX-ERR_APPT_LATE_CANCELLATION.md](ERR_APPT_LATE_CANCELLATION.md)

10. **ERR_APPT_CANCELLATION_REQUIRES_REASON**
    - Regla: RN-APPT-008
    - Descripción: La cancelación requiere motivo obligatorio.
    - [ADR-XX-ERR_APPT_CANCELLATION_REQUIRES_REASON.md](ERR_APPT_CANCELLATION_REQUIRES_REASON.md)

11. **ERR_APPT_MINIMUM_RESCHEDULE_NOTICE**
    - Regla: RN-APPT-012
    - Descripción: No se puede reagendar con menos de 24 horas de anticipación.
    - [ADR-XX-ERR_APPT_MINIMUM_RESCHEDULE_NOTICE.md](ERR_APPT_MINIMUM_RESCHEDULE_NOTICE.md)

---

### Restricciones de rango
12. **ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS**
    - Regla: RN-APPT-013
    - Descripción: La cita no puede cruzar días.
    - [ADR-ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS.md](ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS.md)

---