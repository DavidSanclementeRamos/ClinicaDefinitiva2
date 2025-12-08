# Índice de ADRs — Catálogo de Errores Contract (CON12)

### Flujo de estados
1. **ERR_CONTRACT_INVALID_DATES**
    - Regla: RN-CONTRACT-001
    - Descripción: La fecha de fin debe ser posterior a la fecha de inicio.
    - [ERR_CONTRACT_INVALID_DATES.md](ERR_CONTRACT_INVALID_DATES.md)
2. **ERR_CONTRACT_NOT_EDITABLE**
    - Regla: RN-CONTRACT-002
    - Descripción: Solo puede editarse si está en estado ACTIVE y no vencido.
    - [ERR_CONTRACT_NOT_EDITABLE.md](ERR_CONTRACT_NOT_EDITABLE.md)
3. **ERR_CONTRACT_CANNOT_SUSPEND**
    - Regla: RN-CONTRACT-003
    - Descripción: Solo puede suspenderse si está en estado ACTIVE.
    - [ERR_CONTRACT_CANNOT_SUSPEND.md](ERR_CONTRACT_CANNOT_SUSPEND.md)
4. **ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE**
    - Regla: RN-CONTRACT-004
    - Descripción: No puede reactivarse si está vencido.
    - [ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE.md](ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE.md)
5. **ERR_CONTRACT_INVALID_EXTENSION**
    - Regla: RN-CONTRACT-005
    - Descripción: La extensión de vigencia solo permite fechas posteriores.
    - [ERR_CONTRACT_INVALID_EXTENSION.md](ERR_CONTRACT_INVALID_EXTENSION.md)
6. **ERR_CONTRACT_MISSING_COVERAGE_TYPE**
    - Regla: RN-CONTRACT-006
    - Descripción: Debe tener tipo de cobertura válido.
    - [ERR_CONTRACT_MISSING_COVERAGE_TYPE.md](ERR_CONTRACT_MISSING_COVERAGE_TYPE.md)
7. **ERR_CONTRACT_TERMINATION_REQUIRES_REASON**
    - Regla: RN-CONTRACT-008
    - Descripción: La terminación requiere motivo obligatorio.
    - [ERR_CONTRACT_TERMINATION_REQUIRES_REASON.md](ERR_CONTRACT_TERMINATION_REQUIRES_REASON.md)
---

### Extensión de vigencia
8. **ERR_CONTRACT_MISSING_NEW_END_DATE**
    - Regla: RN-CONTRACT-009
    - Descripción: La nueva fecha de fin es obligatoria.
    - [ERR_CONTRACT_MISSING_NEW_END_DATE.md](ERR_CONTRACT_MISSING_NEW_END_DATE.md)
9. **ERR_CONTRACT_NEW_END_DATE_IN_PAST**
    - Regla: RN-CONTRACT-010
    - Descripción: La nueva fecha de fin no puede estar en el pasado.
    - [ERR_CONTRACT_NEW_END_DATE_IN_PAST.md](ERR_CONTRACT_NEW_END_DATE_IN_PAST.md)
---

### Reactivación y terminación
10. **ERR_CONTRACT_CANNOT_REACTIVATE**
    - Regla: RN-CONTRACT-011
    - Descripción: Solo se pueden reactivar contratos suspendidos.
    - [ERR_CONTRACT_CANNOT_REACTIVATE.md](ERR_CONTRACT_CANNOT_REACTIVATE.md)
11. **ERR_CONTRACT_ALREADY_TERMINATED**
    - Regla: RN-CONTRACT-012
    - Descripción: El contrato ya está terminado.
    - [ERR_CONTRACT_ALREADY_TERMINATED.md](ERR_CONTRACT_ALREADY_TERMINATED.md)
---

### Edición y creación
12. **ERR_CONTRACT_EXPIRED_NOT_EDITABLE**
    - Regla: RN-CONTRACT-013
    - Descripción: No se puede editar un contrato vencido.
    - [ERR_CONTRACT_EXPIRED_NOT_EDITABLE.md](ERR_CONTRACT_EXPIRED_NOT_EDITABLE.md)
13. **ERR_CONTRACT_MISSING_START_DATE**
    - Regla: RN-CONTRACT-014
    - Descripción: La fecha de inicio es obligatoria.
    - [ERR_CONTRACT_MISSING_START_DATE.md](ERR_CONTRACT_MISSING_START_DATE.md)
14. **ERR_CONTRACT_MISSING_END_DATE**
    - Regla: RN-CONTRACT-015
    - Descripción: La fecha de fin es obligatoria.
    - [ERR_CONTRACT_MISSING_END_DATE.md](ERR_CONTRACT_MISSING_END_DATE.md)
---