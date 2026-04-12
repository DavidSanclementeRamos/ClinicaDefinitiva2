

# Índice de ADRs — Catálogo de Errores LedgerAccount (LEDGERACCOUNT)

### Validaciones de código
1. **ERR_ACCOUNT_INVALID_CODE_FORMAT**
    - Regla: RN-LEDGERACCOUNT-002
    - Descripción: Formato de código inválido (solo dígitos numéricos permitidos).
    - [ERR_ACCOUNT_INVALID_CODE_FORMAT.md](ERR_ACCOUNT_INVALID_CODE_FORMAT.md)

2. **ERR_ACCOUNT_INVALID_CODE_LENGTH**
    - Regla: RN-LEDGERACCOUNT-001
    - Descripción: Longitud de código inválida (solo 1, 2, 4, 6 u 8 dígitos).
    - [ERR_ACCOUNT_INVALID_CODE_LENGTH.md](ERR_ACCOUNT_INVALID_CODE_LENGTH.md)

3. **ERR_ACCOUNT_MISSING_CODE**
    - Regla: RN-LEDGERACCOUNT-011
    - Descripción: El código de la cuenta es obligatorio.
    - [ERR_ACCOUNT_MISSING_CODE.md](ERR_ACCOUNT_MISSING_CODE.md)

4. **ERR_ACCOUNT_DUPLICATE_CODE**
    - Regla: RN-LEDGERACCOUNT-009
    - Descripción: Código duplicado dentro de la misma compañía.
    - [ERR_ACCOUNT_DUPLICATE_CODE.md](ERR_ACCOUNT_DUPLICATE_CODE.md)

5. **ERR_ACCOUNT_CANNOT_MODIFY_CODE**
    - Regla: RN-LEDGERACCOUNT-006
    - Descripción: El código de la cuenta no puede modificarse una vez registrado.
    - [ERR_ACCOUNT_CANNOT_MODIFY_CODE.md](ERR_ACCOUNT_CANNOT_MODIFY_CODE.md)

---

### Validaciones de naturaleza
6. **ERR_ACCOUNT_MISSING_NATURE**
    - Regla: RN-LEDGERACCOUNT-003
    - Descripción: La naturaleza de la cuenta es obligatoria.
    - [ERR_ACCOUNT_MISSING_NATURE.md](ERR_ACCOUNT_MISSING_NATURE.md)

---

### Estado y edición de cuentas
7. **ERR_ACCOUNT_ALREADY_ACTIVE**
    - Regla: RN-LEDGERACCOUNT-010
    - Descripción: La cuenta ya está activa.
    - [ERR_ACCOUNT_ALREADY_ACTIVE.md](ERR_ACCOUNT_ALREADY_ACTIVE.md)

8. **ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON**
    - Regla: RN-LEDGERACCOUNT-005
    - Descripción: La inactivación requiere motivo obligatorio.
    - [ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON.md](ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON.md)

9. **ERR_ACCOUNT_NOT_EDITABLE**
    - Regla: RN-LEDGERACCOUNT-004
    - Descripción: La cuenta solo puede editarse si está activa.
    - [ERR_ACCOUNT_NOT_EDITABLE.md](ERR_ACCOUNT_NOT_EDITABLE.md)

---

### Validaciones de movimientos
10. **ERR_ACCOUNT_REQUIRES_DOCUMENT**
    - Regla: RN-LEDGERACCOUNT-008
    - Descripción: La cuenta requiere un documento para registrar el movimiento.
    - [ERR_ACCOUNT_REQUIRES_DOCUMENT.md](ERR_ACCOUNT_REQUIRES_DOCUMENT.md)

11. **ERR_ACCOUNT_REQUIRES_THIRD_PARTY**
    - Regla: RN-LEDGERACCOUNT-007
    - Descripción: La cuenta requiere un tercero para registrar el movimiento.
    - [ERR_ACCOUNT_REQUIRES_THIRD_PARTY.md](ERR_ACCOUNT_REQUIRES_THIRD_PARTY.md)

---


