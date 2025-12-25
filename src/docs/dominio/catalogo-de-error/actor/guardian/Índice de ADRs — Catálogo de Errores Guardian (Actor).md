# Índice de ADRs — Catálogo de Errores Guardian (Actor)

### Consentimiento y autorizaciones
1. **ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT**
    - Regla: RN-GUARDIAN-003
    - Descripción: No se puede revocar consentimiento de un tratamiento ya iniciado.
    - [ADR-XX-ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT.md](ADR-XX-ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT.md)

2. **ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS**
    - Regla: RN-GUARDIAN-005
    - Descripción: No puede desactivarse si tiene autorizaciones clínicas vigentes.
    - [ADR-XX-ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS.md](ADR-XX-ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS.md)

3. **ERR_REVOCATION_REQUIRES_REASON**
    - Regla: RN-GUARDIAN-011
    - Descripción: Revocación de consentimiento requiere motivo obligatorio.
    - [ADR-XX-ERR_REVOCATION_REQUIRES_REASON.md](ADR-XX-ERR_REVOCATION_REQUIRES_REASON.md)

---

### Relación con paciente
4. **ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE**
    - Regla: RN-GUARDIAN-004
    - Descripción: Debe registrarse el tipo de relación con el paciente.
    - [ADR-XX-ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE.md](ADR-XX-ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE.md)

5. **ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP**
    - Regla: RN-GUARDIAN-009
    - Descripción: No puede modificarse vínculo legal si ha autorizado tratamientos.
    - [ADR-XX-ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP.md](ADR-XX-ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP.md)

---

### Edad y límites
6. **ERR_RESPONSIBLE_INVALID_AGE**
    - Regla: RN-GUARDIAN-008
    - Descripción: Responsable debe tener entre 22 y 60 años.
    - [ADR-XX-ERR_RESPONSIBLE_INVALID_AGE.md](ADR-XX-ERR_RESPONSIBLE_INVALID_AGE.md)

7. **ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED**
    - Regla: RN-GUARDIAN-012
    - Descripción: Responsable ha alcanzado límite de pacientes a cargo.
    - [ADR-XX-ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED.md](ADR-XX-ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED.md)

---

### Administración
8. **ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON**
    - Regla: RN-GUARDIAN-010
    - Descripción: La desactivación requiere motivo obligatorio.
    - [ADR-XX-ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON.md](ADR-XX-ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON.md)

---