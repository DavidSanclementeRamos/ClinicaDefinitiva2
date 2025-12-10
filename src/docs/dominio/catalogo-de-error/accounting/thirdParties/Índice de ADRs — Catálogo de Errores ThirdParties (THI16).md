# Índice de ADRs — Catálogo de Errores ThirdParties (TPR01)

### Validaciones de documento
1. **ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH**
    - Regla: RN-THIRDPARTIES-001
    - Descripción: Número de documento debe tener entre 5 y 20 caracteres.
    - [ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH.md](ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH.md)

2. **ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE**
    - Regla: RN-THIRDPARTIES-002
    - Descripción: Tipo de documento es obligatorio.
    - [ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE.md](ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE.md)

3. **ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER**
    - Regla: RN-THIRDPARTIES-003
    - Descripción: Número de documento es obligatorio y único.
    - [ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER.md](ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER.md)

4. **ERR_THIRD_PARTY_INVALID_DOCUMENT_FORMAT**
    - Regla: RN-THIRDPARTIES-009
    - Descripción: Número de documento solo acepta caracteres alfanuméricos.
    - [ERR_THIRD_PARTY_INVALID_DOCUMENT_FORMAT.md](ERR_THIRD_PARTY_INVALID_DOCUMENT_FORMAT.md)

---

### Validaciones de tipo de tercero
5. **ERR_THIRD_PARTY_MISSING_TYPE**
    - Regla: RN-THIRDPARTIES-004
    - Descripción: Tipo de tercero es obligatorio.
    - [ERR_THIRD_PARTY_MISSING_TYPE.md](ERR_THIRD_PARTY_MISSING_TYPE.md)

---

### Estado y edición de terceros
6. **ERR_THIRD_PARTY_NOT_EDITABLE**
    - Regla: RN-THIRDPARTIES-005
    - Descripción: Solo puede editarse si está activo.
    - [ERR_THIRD_PARTY_NOT_EDITABLE.md](ERR_THIRD_PARTY_NOT_EDITABLE.md)

7. **ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON**
    - Regla: RN-THIRDPARTIES-006
    - Descripción: Inactivación requiere motivo obligatorio.
    - [ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON.md](ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON.md)

8. **ERR_THIRD_PARTY_ALREADY_ACTIVE**
    - Regla: RN-THIRDPARTIES-010
    - Descripción: El tercero ya está activo.
    - [ERR_THIRD_PARTY_ALREADY_ACTIVE.md](ERR_THIRD_PARTY_ALREADY_ACTIVE.md)

---

### Integridad del documento
9. **ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT**
    - Regla: RN-THIRDPARTIES-007
    - Descripción: No puede modificarse el número de documento una vez registrado.
    - [ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT.md](ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT.md)

---

### Validaciones de duplicidad
10. **ERR_THIRD_PARTY_DUPLICATE_DOCUMENT**
    - Regla: RN-THIRDPARTIES-008
    - Descripción: Número de documento debe ser único por compañía.
    - [ERR_THIRD_PARTY_DUPLICATE_DOCUMENT.md](ERR_THIRD_PARTY_DUPLICATE_DOCUMENT.md)

---