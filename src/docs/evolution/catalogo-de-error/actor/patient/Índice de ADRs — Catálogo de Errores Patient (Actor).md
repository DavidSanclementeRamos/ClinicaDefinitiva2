# Índice de ADRs — Catálogo de Errores Patient (Actor)

### Servicios y agenda
1. **ERR_PATIENT_ACTIVE_SERVICES**
    - Regla: RN-PATIENT-002
    - Descripción: No puede desactivarse si tiene citas activas o tratamientos en curso.
    - [ADR-XX-ERR_PATIENT_ACTIVE_SERVICES.md](ADR-XX-ERR_PATIENT_ACTIVE_SERVICES.md)

2. **ERR_PATIENT_TIME_CONFLICT**
    - Regla: RN-PATIENT-003
    - Descripción: Paciente ya tiene una cita agendada en este horario.
    - [ADR-XX-ERR_PATIENT_TIME_CONFLICT.md](ADR-XX-ERR_PATIENT_TIME_CONFLICT.md)

---

### Validaciones de edad y responsables
3. **ERR_PATIENT_INVALID_AGE**
    - Regla: RN-PATIENT-006
    - Descripción: Edad del paciente debe estar en rango válido (0–120 años).
    - [ADR-XX-ERR_PATIENT_INVALID_AGE.md](ADR-XX-ERR_PATIENT_INVALID_AGE.md)

4. **ERR_PATIENT_MINOR_REQUIRES_GUARDIAN**
    - Regla: RN-PATIENT-008
    - Descripción: Pacientes menores de edad deben tener responsable legal vinculado.
    - [ADR-XX-ERR_PATIENT_MINOR_REQUIRES_GUARDIAN.md](ADR-XX-ERR_PATIENT_MINOR_REQUIRES_GUARDIAN.md)

---

### Datos clínicos y administración
5. **ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY**
    - Regla: RN-PATIENT-009
    - Descripción: Fecha de nacimiento no puede modificarse si el paciente tiene historial de citas.
    - [ADR-XX-ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY.md](ADR-XX-ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY.md)

6. **ERR_PATIENT_DEACTIVATION_REQUIRES_REASON**
    - Regla: RN-PATIENT-010
    - Descripción: La desactivación requiere motivo obligatorio.
    - [ADR-XX-ERR_PATIENT_DEACTIVATION_REQUIRES_REASON.md](ADR-XX-ERR_PATIENT_DEACTIVATION_REQUIRES_REASON.md)

---

### Turnos asignados
7. **ERR_PATIENT_NO_SHIFT_ASSIGNED**
    - Regla: RN-PATIENT-011
    - Descripción: Paciente no tiene turno asignado.
    - [ADR-XX-ERR_PATIENT_NO_SHIFT_ASSIGNED.md](ADR-XX-ERR_PATIENT_NO_SHIFT_ASSIGNED.md)

8. **ERR_PATIENT_SHIFT_NOT_AVAILABLE**
    - Regla: RN-PATIENT-012
    - Descripción: Horario solicitado no está dentro del turno asignado al paciente.
    - [ADR-XX-ERR_PATIENT_SHIFT_NOT_AVAILABLE.md](ADR-XX-ERR_PATIENT_SHIFT_NOT_AVAILABLE.md)

---
