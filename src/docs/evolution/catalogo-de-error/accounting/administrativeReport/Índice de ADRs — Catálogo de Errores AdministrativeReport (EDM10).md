

# Índice de ADRs — Catálogo de Errores AdministrativeReport (EDM10)

### Flujo de estados
1. **ERR_REPORT_NOT_EDITABLE**
    - Regla: RN-ADMINREPORT-001
    - Descripción: Reporte no editable fuera de estado DRAFT.
    - [ADR-XX-ERR_REPORT_NOT_EDITABLE.md](ADR-XX-ERR_REPORT_NOT_EDITABLE.md)

2. **ERR_REPORT_INCOMPLETE**
    - Regla: RN-ADMINREPORT-002
    - Descripción: Reporte incompleto para envío a revisión.
    - [ADR-XX-ERR_REPORT_INCOMPLETE.md](ADR-XX-ERR_REPORT_INCOMPLETE.md)

3. **ERR_REPORT_CANNOT_SUBMIT**
    - Regla: RN-ADMINREPORT-003
    - Descripción: Reporte no puede enviarse a revisión desde estado inválido.
    - [ADR-XX-ERR_REPORT_CANNOT_SUBMIT.md](ADR-XX-ERR_REPORT_CANNOT_SUBMIT.md)

4. **ERR_REPORT_CANNOT_APPROVE**
    - Regla: RN-ADMINREPORT-004
    - Descripción: Reporte no puede aprobarse fuera de estado revisión.
    - [ADR-XX-ERR_REPORT_CANNOT_APPROVE.md](ADR-XX-ERR_REPORT_CANNOT_APPROVE.md)

5. **ERR_REPORT_CANNOT_REJECT**
    - Regla: RN-ADMINREPORT-005
    - Descripción: Reporte no puede rechazarse fuera de estado revisión.
    - [ADR-XX-ERR_REPORT_CANNOT_REJECT.md](ADR-XX-ERR_REPORT_CANNOT_REJECT.md)

6. **ERR_REPORT_REJECTION_REQUIRES_REASON**
    - Regla: RN-ADMINREPORT-006
    - Descripción: Rechazo de reporte requiere motivo obligatorio.
    - [ADR-XX-ERR_REPORT_REJECTION_REQUIRES_REASON.md](ADR-XX-ERR_REPORT_REJECTION_REQUIRES_REASON.md)

7. **ERR_REPORT_CANNOT_ARCHIVE**
    - Regla: RN-ADMINREPORT-007
    - Descripción: Reporte no puede archivarse fuera de estado publicado.
    - [ADR-XX-ERR_REPORT_CANNOT_ARCHIVE.md](ADR-XX-ERR_REPORT_CANNOT_ARCHIVE.md)
8. **ERR_REPORT_CANNOT_UNARCHIVE**
    - Regla: RN-ADMINREPORT-008
    - Descripción: Reporte no puede desarchivarse fuera de estado archivado.
    - [ADR-XX-ERR_REPORT_CANNOT_UNARCHIVE.md](ADR-XX-ERR_REPORT_CANNOT_UNARCHIVE.md)

---

### Asientos contables
9. **ERR_REPORT_DUPLICATE_JOURNAL_ENTRY**
    - Regla: RN-ADMINREPORT-009
    - Descripción: Referencia duplicada a asiento contable.
    - [ADR-XX-ERR_REPORT_DUPLICATE_JOURNAL_ENTRY.md](ADR-XX-ERR_REPORT_DUPLICATE_JOURNAL_ENTRY.md)

10. **ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND**
    - Regla: RN-ADMINREPORT-010
    - Descripción: Referencia a asiento contable inexistente.
    - [ADR-XX-ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND.md](ADR-XX-ERR_REPORT_JOURNAL_ENTRY_NOT_FOUND.md)

---

### Aprobación
11. **ERR_REPORT_MISSING_APPROVER**
    - Regla: RN-ADMINREPORT-011
    - Descripción: Aprobación requiere usuario aprobador válido.
    - [ADR-XX-ERR_REPORT_MISSING_APPROVER.md](ADR-XX-ERR_REPORT_MISSING_APPROVER.md)

---
