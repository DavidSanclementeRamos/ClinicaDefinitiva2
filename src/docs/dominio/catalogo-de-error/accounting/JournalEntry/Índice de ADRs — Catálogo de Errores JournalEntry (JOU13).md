# Índice de ADRs — Catálogo de Errores JournalEntry (JOU13)

### Validaciones de cuenta y monto
1. **ERR_JOURNALENTRY_MISSING_ACCOUNT**
    - Regla: RN-JOURNALENTRY-001
    - Descripción: Asiento sin cuenta contable asociada.
    - [ERR_JOURNALENTRY_MISSING_ACCOUNT.md](ERR_JOURNALENTRY_MISSING_ACCOUNT.md)
2. **ERR_JOURNALENTRY_INVALID_AMOUNT**
    - Regla: RN-JOURNALENTRY-002
    - Descripción: Monto inválido (cero o negativo).
    - [ERR_JOURNALENTRY_INVALID_AMOUNT.md](ERR_JOURNALENTRY_INVALID_AMOUNT.md)
3. **ERR_JOURNALENTRY_MISSING_AMOUNT**
    - Regla: RN-JOURNALENTRY-023
    - Descripción: El monto es obligatorio.
    - [ERR_JOURNALENTRY_MISSING_AMOUNT.md](ERR_JOURNALENTRY_MISSING_AMOUNT.md)
---

### Balance y referencias
4. **ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH**
    - Regla: RN-JOURNALENTRY-003
    - Descripción: Débitos y créditos desbalanceados.
    - [ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH.md](ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH.md)
5. **ERR_JOURNALENTRY_DUPLICATE_REFERENCE**
    - Regla: RN-JOURNALENTRY-004
    - Descripción: Referencia duplicada de asiento contable.
    - [ERR_JOURNALENTRY_DUPLICATE_REFERENCE.md](ERR_JOURNALENTRY_DUPLICATE_REFERENCE.md)
---

### Validaciones de fecha
6. **ERR_JOURNALENTRY_DATE_IN_FUTURE**
    - Regla: RN-JOURNALENTRY-005
    - Descripción: Fecha del asiento no puede estar en el futuro.
    - [ERR_JOURNALENTRY_DATE_IN_FUTURE.md](ERR_JOURNALENTRY_DATE_IN_FUTURE.md)
7. **ERR_JOURNALENTRY_DATE_BEFORE_PERIOD**
    - Regla: RN-JOURNALENTRY-006
    - Descripción: Fecha anterior al período contable vigente.
    - [ERR_JOURNALENTRY_DATE_BEFORE_PERIOD.md](ERR_JOURNALENTRY_DATE_BEFORE_PERIOD.md)
8. **ERR_JOURNALENTRY_MISSING_DATE**
    - Regla: RN-JOURNALENTRY-022
    - Descripción: La fecha es obligatoria.
    - [ERR_JOURNALENTRY_MISSING_DATE.md](ERR_JOURNALENTRY_MISSING_DATE.md)
---

### Validaciones de descripción y documento
9. **ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD**
    - Regla: RN-JOURNALENTRY-007
    - Descripción: La descripción es obligatoria.
    - [ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD.md](ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD.md)
10. **ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH**
    - Regla: RN-JOURNALENTRY-020
    - Descripción: La descripción debe tener al menos 5 caracteres.
    - [ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH.md](ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH.md)
11. **ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER**
    - Regla: RN-JOURNALENTRY-018
    - Descripción: El número de documento es obligatorio.
    - [ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER.md](ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER.md)
12. **ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER**
    - Regla: RN-JOURNALENTRY-019
    - Descripción: El número de documento debe tener al menos 1 carácter.
    - [ADR-XX-ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER.md](ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER.md)

---

### Restricciones de edición y eliminación
13. **ERR_JOURNALENTRY_NOT_EDITABLE**
    - Regla: RN-JOURNALENTRY-008
    - Descripción: El asiento no puede editarse una vez publicado.
    - [ADR-XX-ERR_JOURNALENTRY_NOT_EDITABLE.md](ERR_JOURNALENTRY_NOT_EDITABLE.md)

14. **ERR_JOURNALENTRY_CANNOT_DELETE**
    - Regla: RN-JOURNALENTRY-009
    - Descripción: El asiento no puede eliminarse si está conciliado.
    - [ADR-XX-ERR_JOURNALENTRY_CANNOT_DELETE.md](ERR_JOURNALENTRY_CANNOT_DELETE.md)

---

### Autorización
15. **ERR_JOURNALENTRY_UNAUTHORIZED_USER**
    - Regla: RN-JOURNALENTRY-010
    - Descripción: Usuario no autorizado para registrar asientos contables.
    - [ADR-XX-ERR_JOURNALENTRY_UNAUTHORIZED_USER.md](ERR_JOURNALENTRY_UNAUTHORIZED_USER.md)

---

### Validaciones de líneas
16. **ERR_JOURNALENTRY_LINE_NOT_FOUND**
    - Regla: RN-JOURNALENTRY-011
    - Descripción: Línea inexistente en el asiento.
    - [ADR-XX-ERR_JOURNALENTRY_LINE_NOT_FOUND.md](ERR_JOURNALENTRY_LINE_NOT_FOUND.md)

17. **ERR_JOURNALENTRY_EMPTY**
    - Regla: RN-JOURNALENTRY-012
    - Descripción: El asiento debe tener al menos una línea.
    - [ADR-XX-ERR_JOURNALENTRY_EMPTY.md](ERR_JOURNALENTRY_EMPTY.md)

18. **ERR_JOURNALENTRY_INSUFFICIENT_LINES**
    - Regla: RN-JOURNALENTRY-013
    - Descripción: El asiento debe tener al menos dos líneas (partida doble).
    - [ADR-XX-ERR_JOURNALENTRY_INSUFFICIENT_LINES.md](ERR_JOURNALENTRY_INSUFFICIENT_LINES.md)

---

### Contabilización
19. **ERR_JOURNALENTRY_ALREADY_POSTED**
    - Regla: RN-JOURNALENTRY-014
    - Descripción: El asiento ya está contabilizado.
    - [ADR-XX-ERR_JOURNALENTRY_ALREADY_POSTED.md](ERR_JOURNALENTRY_ALREADY_POSTED.md)

20. **ERR_JOURNALENTRY_FUTURE_DATE**
    - Regla: RN-JOURNALENTRY-015
    - Descripción: No se puede contabilizar un asiento con fecha futura.
    - [ADR-XX-ERR_JOURNALENTRY_FUTURE_DATE.md](ERR_JOURNALENTRY_FUTURE_DATE.md)

---

### Reversas
21. **ERR_JOURNALENTRY_NOT_POSTED_REVERSAL**
    - Regla: RN-JOURNALENTRY-016
    - Descripción: Solo se pueden reversar asientos contabilizados.
    - [ADR-XX-ERR_JOURNALENTRY_NOT_POSTED_REVERSAL.md](ERR_JOURNALENTRY_NOT_POSTED_REVERSAL.md)

22. **ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON**
    - Regla: RN-JOURNALENTRY-017
    - Descripción: Se requiere una razón para reversar el asiento.
    - [ADR-XX-ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON.md](ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON.md)

---