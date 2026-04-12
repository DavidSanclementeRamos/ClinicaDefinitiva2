

# Índice de ADRs — Catálogo de Errores OpeningBalance (OPE16)

### Validaciones de monto
1. **ERR_OPENING_BALANCE_INVALID_AMOUNT**
    - Regla: RN-OPENINGBALANCE-001
    - Descripción: El monto debe ser mayor a cero.
    - [ERR_OPENING_BALANCE_INVALID_AMOUNT.md](ERR_OPENING_BALANCE_INVALID_AMOUNT.md)

2. **ERR_OPENING_BALANCE_MISSING_AMOUNT**
    - Regla: RN-OPENINGBALANCE-002
    - Descripción: El monto es obligatorio.
    - [ERR_OPENING_BALANCE_MISSING_AMOUNT.md](ERR_OPENING_BALANCE_MISSING_AMOUNT.md)

---

### Validaciones de fecha
3. **ERR_OPENING_BALANCE_MISSING_DATE**
    - Regla: RN-OPENINGBALANCE-003
    - Descripción: La fecha es obligatoria.
    - [ERR_OPENING_BALANCE_MISSING_DATE.md](ERR_OPENING_BALANCE_MISSING_DATE.md)

---

### Validaciones de cuenta y compañía
4. **ERR_OPENING_BALANCE_MISSING_ACCOUNT**
    - Regla: RN-OPENINGBALANCE-004
    - Descripción: Debe tener cuenta contable válida.
    - [ERR_OPENING_BALANCE_MISSING_ACCOUNT.md](ERR_OPENING_BALANCE_MISSING_ACCOUNT.md)

5. **ERR_OPENING_BALANCE_MISSING_COMPANY**
    - Regla: RN-OPENINGBALANCE-005
    - Descripción: Debe tener compañía válida.
    - [ERR_OPENING_BALANCE_MISSING_COMPANY.md](ERR_OPENING_BALANCE_MISSING_COMPANY.md)

---

### Estado y edición de saldos
6. **ERR_OPENING_BALANCE_IMMUTABLE**
    - Regla: RN-OPENINGBALANCE-006
    - Descripción: No permite edición una vez registrado (inmutable).
    - [ERR_OPENING_BALANCE_IMMUTABLE.md](ERR_OPENING_BALANCE_IMMUTABLE.md)

---

### Validaciones de terceros
7. **ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY**
    - Regla: RN-OPENINGBALANCE-007
    - Descripción: Si la cuenta requiere tercero, debe incluir tercero.
    - [ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY.md](ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY.md)

---

### Validaciones de duplicidad
8. **ERR_OPENING_BALANCE_DUPLICATE**
    - Regla: RN-OPENINGBALANCE-008
    - Descripción: No puede registrarse saldo duplicado para misma cuenta/tercero/período.
    - [ERR_OPENING_BALANCE_DUPLICATE.md](ERR_OPENING_BALANCE_DUPLICATE.md)

---
