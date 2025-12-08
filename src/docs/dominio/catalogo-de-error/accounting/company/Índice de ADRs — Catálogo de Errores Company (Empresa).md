
# Índice de ADRs — Catálogo de Errores Company (Empresa)

### Creación y datos obligatorios
1. **ERR_COMPANY_MISSING_TAX_ID**
  - Regla: RN-COMPANY-001
  - Descripción: Empresa sin NIT registrado.
  - [ERR_COMPANY_MISSING_TAX_ID.md](ERR_COMPANY_MISSING_TAX_ID.md)
2. **ERR_COMPANY_MISSING_PERSON_TYPE**
  - Regla: RN-COMPANY-005
  - Descripción: Tipo de persona no especificado.
  - [ERR_COMPANY_MISSING_PERSON_TYPE.md](ERR_COMPANY_MISSING_PERSON_TYPE.md)
3. **ERR_COMPANY_MISSING_INCORPORATION_DATE**
  - Regla: RN-COMPANY-007
  - Descripción: Falta fecha de constitución.
  - [ERR_COMPANY_MISSING_INCORPORATION_DATE.md](ERR_COMPANY_MISSING_INCORPORATION_DATE.md)
4. **ERR_COMPANY_MISSING_CONTACT**
  - Regla: RN-COMPANY-008
  - Descripción: Falta medio de contacto válido.
  - [ERR_COMPANY_MISSING_CONTACT.md](ERR_COMPANY_MISSING_CONTACT.md)
---

### Validación de fechas de constitución
5. **ERR_COMPANY_FUTURE_INCORPORATION_DATE**
  - Regla: RN-COMPANY-002
  - Descripción: Fecha de constitución futura inválida.
  - [ERR_COMPANY_FUTURE_INCORPORATION_DATE .md](ERR_COMPANY_FUTURE_INCORPORATION_DATE%20.md)
6. **ERR_COMPANY_INVALID_INCORPORATION_DATE**
  - Regla: RN-COMPANY-009
  - Descripción: Fecha de constitución inferior a 1800.
  - [ERR_COMPANY_INVALID_INCORPORATION_DATE.md](ERR_COMPANY_INVALID_INCORPORATION_DATE.md)
---

### Edición y restricciones de cambio
7. **ERR_COMPANY_NOT_EDITABLE**
  - Regla: RN-COMPANY-003
  - Descripción: Empresa no editable en estado INACTIVE.
  - [ERR_COMPANY_NOT_EDITABLE.md](ERR_COMPANY_NOT_EDITABLE.md)
8. **ERR_COMPANY_CANNOT_MODIFY_TAX_ID**
  - Regla: RN-COMPANY-006
  - Descripción: NIT inmutable tras creación.
  - [ERR_COMPANY_CANNOT_MODIFY_TAX_ID.md](ERR_COMPANY_CANNOT_MODIFY_TAX_ID.md)
---

### Estado operativo
9. **ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY**
  - Regla: RN-COMPANY-004
  - Descripción: Reactivación directa prohibida.
  - [ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY.md](ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY.md)
---
