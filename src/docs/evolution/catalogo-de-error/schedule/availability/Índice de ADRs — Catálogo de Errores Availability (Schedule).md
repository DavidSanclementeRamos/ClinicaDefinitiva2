

# Índice de ADRs — Catálogo de Errores Availability (Schedule)

### Validaciones de rango y duración
1. **ERR_AVAIL_INVALID_TIME_RANGE**
    - Regla: RN-AVAIL-001
    - Descripción: La hora de inicio debe ser anterior a la hora de fin.
    - [ADR-XX-ERR_AVAIL_INVALID_TIME_RANGE.md](ADR-XX-ERR_AVAIL_INVALID_TIME_RANGE.md)

2. **ERR_AVAIL_ZERO_DURATION**
    - Regla: RN-AVAIL-002
    - Descripción: No puede crearse disponibilidad con duración negativa o cero.
    - [ADR-XX-ERR_AVAIL_ZERO_DURATION.md](ADR-XX-ERR_AVAIL_ZERO_DURATION.md)

---

### Conflictos y solapamientos
3. **ERR_AVAIL_OVERLAP_CONFLICT**
    - Regla: RN-AVAIL-004
    - Descripción: No puede haber dos bloques que se solapen para el mismo profesional.
    - [ADR-XX-ERR_AVAIL_OVERLAP_CONFLICT.md](ADR-XX-ERR_AVAIL_OVERLAP_CONFLICT.md)

4. **ERR_AVAIL_EXTENSION_CONFLICT**
    - Regla: RN-AVAIL-009
    - Descripción: No puede extenderse sobre otro bloque ya registrado.
    - [ADR-XX-ERR_AVAIL_EXTENSION_CONFLICT.md](ADR-XX-ERR_AVAIL_EXTENSION_CONFLICT.md)

---

### Reglas de activación y desactivación
5. **ERR_AVAIL_DEACTIVATION_REQUIRES_REASON**
    - Regla: RN-AVAIL-008
    - Descripción: La desactivación requiere motivo obligatorio.
    - [ADR-XX-ERR_AVAIL_DEACTIVATION_REQUIRES_REASON.md](ADR-XX-ERR_AVAIL_DEACTIVATION_REQUIRES_REASON.md)

6. **ERR_AVAIL_INVALID_DEACTIVATION**
    - Regla: RN-AVAIL-013
    - Descripción: No puede desactivarse la disponibilidad en el estado actual.
    - [ADR-XX-ERR_AVAIL_INVALID_DEACTIVATION.md](ADR-XX-ERR_AVAIL_INVALID_DEACTIVATION.md)

7. **ERR_AVAIL_INVALID_ACTIVATION**
    - Regla: RN-AVAIL-014
    - Descripción: No puede activarse la disponibilidad en el estado actual.
    - [ADR-XX-ERR_AVAIL_INVALID_ACTIVATION.md](ADR-XX-ERR_AVAIL_INVALID_ACTIVATION.md)

---

### Reglas de creación obligatoria
8. **ERR_AVAIL_DENTIST_REQUIRED**
    - Regla: RN-AVAIL-010
    - Descripción: Debe especificarse un DentistId válido para crear disponibilidad.
    - [ADR-XX-ERR_AVAIL_DENTIST_REQUIRED.md](ADR-XX-ERR_AVAIL_DENTIST_REQUIRED.md)

9. **ERR_AVAIL_DAY_REQUIRED**
    - Regla: RN-AVAIL-011
    - Descripción: Debe especificarse un día de la semana válido para crear disponibilidad.
    - [ADR-XX-ERR_AVAIL_DAY_REQUIRED.md](ADR-XX-ERR_AVAIL_DAY_REQUIRED.md)

10. **ERR_AVAIL_TIME_REQUIRED**
    - Regla: RN-AVAIL-012
    - Descripción: Debe especificarse hora de inicio y fin para crear disponibilidad.
    - [ADR-XX-ERR_AVAIL_TIME_REQUIRED.md](ADR-XX-ERR_AVAIL_TIME_REQUIRED.md)

