# Índice de ADRs — Catálogo de Errores Shift (Turno Operativo)

### Validaciones de rango y solapamiento
1. **ERR_SHIFT_INVALID_TIME_RANGE**
    - Regla: RN-SHIFT-001
    - Descripción: La hora de inicio debe ser anterior a la hora de fin.
    - [Schedule-ERR_SHIFT_INVALID_TIME_RANGE.md](Schedule-ERR_SHIFT_INVALID_TIME_RANGE.md)

2. **ERR_SHIFT_OVERLAP_CONFLICT**
    - Regla: RN-SHIFT-003
    - Descripción: No puede solaparse con otro turno del mismo profesional.
    - [Schedule-ERR_SHIFT_OVERLAP_CONFLICT.md](Schedule-ERR_SHIFT_OVERLAP_CONFLICT.md)

---

### Cancelaciones y modificaciones
3. **ERR_SHIFT_CANCELLATION_REQUIRES_REASON**
    - Regla: RN-SHIFT-007
    - Descripción: La cancelación requiere motivo obligatorio.
    - [Schedule-ERR_SHIFT_CANCELLATION_REQUIRES_REASON.md](Schedule-ERR_SHIFT_CANCELLATION_REQUIRES_REASON.md)

4. **ERR_SHIFT_ZERO_DURATION**
    - Regla: RN-SHIFT-008
    - Descripción: No puede tener duración negativa o cero.
    - [Schedule-ERR_SHIFT_ZERO_DURATION.md](Schedule-ERR_SHIFT_ZERO_DURATION.md)

5. **ERR_SHIFT_LATE_MODIFICATION**
    - Regla: RN-SHIFT-009
    - Descripción: No puede modificarse si está dentro de 24h previas sin autorización.
    - [Schedule-ERR_SHIFT_LATE_MODIFICATION.md](Schedule-ERR_SHIFT_LATE_MODIFICATION.md)

---

### Creación obligatoria
6. **ERR_SHIFT_DENTIST_REQUIRED**
    - Regla: RN-SHIFT-010
    - Descripción: Debe especificarse un DentistId válido para crear un turno.
    - [Schedule-ERR_SHIFT_DENTIST_REQUIRED.md](Schedule-ERR_SHIFT_DENTIST_REQUIRED.md)

7. **ERR_SHIFT_DATE_REQUIRED**
    - Regla: RN-SHIFT-011
    - Descripción: Debe especificarse una fecha válida para crear un turno.
    - [Schedule-ERR_SHIFT_DATE_REQUIRED.md](Schedule-ERR_SHIFT_DATE_REQUIRED.md)

8. **ERR_SHIFT_TIME_REQUIRED**
    - Regla: RN-SHIFT-012
    - Descripción: Debe especificarse hora de inicio y fin para crear un turno.
    - [Schedule-ERR_SHIFT_TIME_REQUIRED.md](Schedule-ERR_SHIFT_TIME_REQUIRED.md)

9. **ERR_SHIFT_TYPE_REQUIRED**
    - Regla: RN-SHIFT-013
    - Descripción: Debe especificarse un tipo de turno válido.
    - [Schedule-ERR_SHIFT_TYPE_REQUIRED.md](Schedule-ERR_SHIFT_TYPE_REQUIRED.md)

---

### Reprogramación y cobertura
10. **ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED**
    - Regla: RN-SHIFT-014
    - Descripción: Debe especificarse nueva fecha y horas de inicio y fin para reprogramar el turno.
    - [Schedule-ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED.md](Schedule-ERR_SHIFT_RESCHEDULE_PARAMETERS_REQUIRED.md)

11. **ERR_SHIFT_OVERLAP_TARGET_REQUIRED**
    - Regla: RN-SHIFT-015
    - Descripción: Debe especificarse un turno válido para evaluar solapamiento.
    - [Schedule-ERR_SHIFT_OVERLAP_TARGET_REQUIRED.md](Schedule-ERR_SHIFT_OVERLAP_TARGET_REQUIRED.md)

12. **ERR_SHIFT_NO_ACTIVE_COVERAGE**
    - Regla: RN-SHIFT-016
    - Descripción: El dentista no tiene turno activo en ese horario.
    - [Schedule-ERR_SHIFT_NO_ACTIVE_COVERAGE.md](Schedule-ERR_SHIFT_NO_ACTIVE_COVERAGE.md)

---