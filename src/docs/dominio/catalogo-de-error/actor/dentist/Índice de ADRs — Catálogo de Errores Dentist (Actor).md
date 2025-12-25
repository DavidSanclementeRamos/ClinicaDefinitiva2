# Índice de ADRs — Catálogo de Errores Dentist (Actor)

### Reglas de edad y disponibilidad
1. **ERR_DENTIST_AGE_INSUFFICIENT**
    - Regla: RN-DENTIST-001
    - Descripción: Odontólogo debe tener al menos 25 años.
    - [ADR-XX-ERR_DENTIST_AGE_INSUFFICIENT.md](ADR-XX-ERR_DENTIST_AGE_INSUFFICIENT.md)

2. **ERR_DENTIST_MISSING_AVAILABILITY**
    - Regla: RN-DENTIST-002
    - Descripción: Odontólogo debe registrar disponibilidad inicial mínima.
    - [ADR-XX-ERR_DENTIST_MISSING_AVAILABILITY.md](ADR-XX-ERR_DENTIST_MISSING_AVAILABILITY.md)

---

### Restricciones de agenda y citas
3. **ERR_DENTIST_ACTIVE_APPOINTMENTS**
    - Regla: RN-DENTIST-003
    - Descripción: No puede desactivarse si tiene citas activas en las próximas 24 horas.
    - [ADR-XX-ERR_DENTIST_ACTIVE_APPOINTMENTS.md](ADR-XX-ERR_DENTIST_ACTIVE_APPOINTMENTS.md)

4. **ERR_DENTIST_TIME_CONFLICT**
    - Regla: RN-DENTIST-004
    - Descripción: Conflicto de horario, odontólogo ya tiene cita en ese horario.
    - [ADR-XX-ERR_DENTIST_TIME_CONFLICT.md](ADR-XX-ERR_DENTIST_TIME_CONFLICT.md)

5. **ERR_DENTIST_NOT_AVAILABLE**
    - Regla: RN-DENTIST-005
    - Descripción: Odontólogo no disponible para agendar en este momento.
    - [ADR-XX-ERR_DENTIST_NOT_AVAILABLE.md](ADR-XX-ERR_DENTIST_NOT_AVAILABLE.md)

---

### Validaciones de especialidad y disponibilidad
6. **ERR_DENTIST_INVALID_SPECIALTY**
    - Regla: RN-DENTIST-007
    - Descripción: Especialidad proporcionada no es reconocida.
    - [ADR-XX-ERR_DENTIST_INVALID_SPECIALTY.md](ADR-XX-ERR_DENTIST_INVALID_SPECIALTY.md)

7. **ERR_DENTIST_EMPTY_AVAILABILITY**
    - Regla: RN-DENTIST-010
    - Descripción: La disponibilidad del odontólogo no puede quedar vacía.
    - [ADR-XX-ERR_DENTIST_EMPTY_AVAILABILITY.md](ADR-XX-ERR_DENTIST_EMPTY_AVAILABILITY.md)

---

### Horarios laborales y vacaciones
8. **ERR_DENTIST_OUT_OF_WORKING_HOURS**
    - Regla: RN-DENTIST-011
    - Descripción: Horario solicitado fuera de horas laborales declaradas.
    - [ADR-XX-ERR_DENTIST_OUT_OF_WORKING_HOURS.md](ERR_DENTIST_OUT_OF_WORKING_HOURS.md)

9. **ERR_DENTIST_INVALID_VACATION_RANGE**
    - Regla: RN-DENTIST-012
    - Descripción: Rango de vacaciones solicitado inválido.
    - [ADR-XX-ERR_DENTIST_INVALID_VACATION_RANGE.md](ADR-XX-ERR_DENTIST_INVALID_VACATION_RANGE.md)

10. **ERR_DENTIST_VACATION_CONFLICT**
    - Regla: RN-DENTIST-013
    - Descripción: Vacaciones en conflicto con citas agendadas.
    - [ADR-XX-ERR_DENTIST_VACATION_CONFLICT.md](ADR-XX-ERR_DENTIST_VACATION_CONFLICT.md)

11. **ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS**
    - Regla: RN-DENTIST-014
    - Descripción: Reagendación fuera de horas laborales del odontólogo.
    - [ADR-XX-ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS.md](ADR-XX-ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS.md)

---
