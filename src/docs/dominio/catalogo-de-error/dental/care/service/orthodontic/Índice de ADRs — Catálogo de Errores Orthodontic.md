# Índice de ADRs — Catálogo de Errores Orthodontic

### Validaciones de tipo de aparato
1. **ERR_ORTHODONTIC_MISSING_APPLIANCE**
    - **Regla:** RN-ORTHODONTIC-001
    - **Descripción:** El tipo de aparato es obligatorio y no puede estar en blanco.
    - [ADR-XX-ERR_ORTHODONTIC_MISSING_APPLIANCE.md](ADR-XX-ERR_ORTHODONTIC_MISSING_APPLIANCE.md)

2. **ERR_ORTHODONTIC_INVALID_APPLIANCE**
    - **Regla:** RN-ORTHODONTIC-003
    - **Descripción:** El tipo de aparato debe ser reconocido por el sistema.
    - [ADR-XX-ERR_ORTHODONTIC_INVALID_APPLIANCE.md](ADR-XX-ERR_ORTHODONTIC_INVALID_APPLIANCE.md)

---

### Validaciones de duración del tratamiento
3. **ERR_ORTHODONTIC_INVALID_DURATION**
    - **Regla:** RN-ORTHODONTIC-002
    - **Descripción:** La duración del tratamiento debe estar entre 6 y 48 meses.
    - [ADR-XX-ERR_ORTHODONTIC_INVALID_DURATION.md](ADR-XX-ERR_ORTHODONTIC_INVALID_DURATION.md)

4. **ERR_ORTHODONTIC_NEGATIVE_DURATION**
    - **Regla:** RN-ORTHODONTIC-004
    - **Descripción:** La duración del tratamiento debe ser positiva.
    - [ADR-XX-ERR_ORTHODONTIC_NEGATIVE_DURATION.md](ADR-XX-ERR_ORTHODONTIC_NEGATIVE_DURATION.md)

---

### Advertencias sobre duraciones atípicas
5. **WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION**
    - **Regla:** RN-ORTHODONTIC-005
    - **Descripción:** Alineadores transparentes típicamente duran 12-24 meses.
    - [ADR-XX-WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION.md](ADR-XX-WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION.md)

6. **WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION**
    - **Regla:** RN-ORTHODONTIC-006
    - **Descripción:** Brackets linguales deben tener duración mínima de 18 meses.
    - [ADR-XX-WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION.md](ADR-XX-WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION.md)

---

