# Índice de ADRs — Catálogo de Errores Receptionist (Actor)

### Confirmación y agendamiento
1. **ERR_RECEPTIONIST_DENTIST_INACTIVE**
    - Regla: RN-RECEPTIONIST-001
    - Descripción: No puede confirmar citas para odontólogos inactivos.
    - [ADR-XX-ERR_RECEPTIONIST_DENTIST_INACTIVE.md](ADR-XX-ERR_RECEPTIONIST_DENTIST_INACTIVE.md)

2. **ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT**
    - Regla: RN-RECEPTIONIST-002
    - Descripción: No puede agendar citas duplicadas para el mismo paciente en el mismo horario.
    - [ADR-XX-ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT.md](ADR-XX-ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT.md)

---

### Cancelaciones y sedes
3. **ERR_RECEPTIONIST_LATE_CANCELLATION**
    - Regla: RN-RECEPTIONIST-003
    - Descripción: Solo puede cancelar citas si no están dentro de las 24h previas.
    - [ADR-XX-ERR_RECEPTIONIST_LATE_CANCELLATION.md](ADR-XX-ERR_RECEPTIONIST_LATE_CANCELLATION.md)

4. **ERR_RECEPTIONIST_INVALID_LOCATION**
    - Regla: RN-RECEPTIONIST-004
    - Descripción: Recepcionista debe estar asociado a una sede válida.
    - [ADR-XX-ERR_RECEPTIONIST_INVALID_LOCATION.md](ADR-XX-ERR_RECEPTIONIST_INVALID_LOCATION.md)

---

### Tareas y asignaciones
5. **ERR_RECEPTIONIST_HAS_PENDING_TASKS**
    - Regla: RN-RECEPTIONIST-006
    - Descripción: No puede desactivarse si tiene tareas pendientes.
    - [ADR-XX-ERR_RECEPTIONIST_HAS_PENDING_TASKS.md](ADR-XX-ERR_RECEPTIONIST_HAS_PENDING_TASKS.md)

6. **ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS**
    - Regla: RN-RECEPTIONIST-007
    - Descripción: No puede modificar sede si tiene citas asignadas en curso.
    - [ADR-XX-ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS.md](ADR-XX-ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS.md)

---

### Administración
7. **ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER**
    - Regla: RN-RECEPTIONIST-008
    - Descripción: Solo se pueden registrar recepcionistas con usuarios activos.
    - [ADR-XX-ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER.md](ADR-XX-ERR_RECEPTIONIST_CREATION_REQUIRES_ACTIVE_USER.md)

8. **ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON**
    - Regla: RN-RECEPTIONIST-010
    - Descripción: La desactivación requiere motivo obligatorio.
    - [ADR-XX-ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON.md](ADR-XX-ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON.md)

---