# Índice de ADRs — Catálogo de Errores Prosthetic

### Validaciones de tipo y unidades
1. **ERR_PROSTHETIC_MISSING_TYPE**
    - Regla: RN-PROSTHETIC-001
    - Descripción: Debe especificar si la prótesis es fija o removible.
    - [ADR-XX-ERR_PROSTHETIC_MISSING_TYPE.md](ADR-XX-ERR_PROSTHETIC_MISSING_TYPE.md)

2. **ERR_PROSTHETIC_INVALID_UNITS**
    - Regla: RN-PROSTHETIC-002
    - Descripción: El número de unidades debe ser mayor o igual a 0.
    - [ADR-XX-ERR_PROSTHETIC_INVALID_UNITS.md](ADR-XX-ERR_PROSTHETIC_INVALID_UNITS.md)

3. **ERR_PROSTHETIC_EXCESSIVE_UNITS**
    - Regla: RN-PROSTHETIC-003
    - Descripción: Prótesis removibles no pueden tener más de 14 unidades por arcada.
    - [ADR-XX-ERR_PROSTHETIC_EXCESSIVE_UNITS.md](ADR-XX-ERR_PROSTHETIC_EXCESSIVE_UNITS.md)

4. **ERR_PROSTHETIC_INVALID_TYPE_VALUE**
    - Regla: RN-PROSTHETIC-004
    - Descripción: El tipo debe ser FIXED (fija) o REMOVABLE (removible).
    - [ADR-XX-ERR_PROSTHETIC_INVALID_TYPE_VALUE.md](ADR-XX-ERR_PROSTHETIC_INVALID_TYPE_VALUE.md)

---

### Advertencias sobre diseño y unidades protésicas
5. **WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS**
    - Regla: RN-PROSTHETIC-005
    - Descripción: Corona individual típicamente tiene 1 unidad.
    - [ADR-XX-WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS.md](ADR-XX-WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS.md)

6. **WARN_PROSTHETIC_FULL_DENTURE_UNITS**
    - Regla: RN-PROSTHETIC-006
    - Descripción: Prótesis total típicamente tiene 14 unidades por arcada.
    - [ADR-XX-WARN_PROSTHETIC_FULL_DENTURE_UNITS.md](ADR-XX-WARN_PROSTHETIC_FULL_DENTURE_UNITS.md)

7. **WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS**
    - Regla: RN-PROSTHETIC-007
    - Descripción: Puente fijo típicamente tiene al menos 3 unidades.
    - [ADR-XX-WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS.md](ADR-XX-WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS.md)

---


