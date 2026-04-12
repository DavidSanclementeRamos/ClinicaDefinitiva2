## Índice de ADRs — Catálogo de Errores Implantology

#### Validaciones de tiempo de cicatrización
1. **ERR_IMPLANTOLOGY_INVALID_HEALING_TIME**
    - Regla: RN-IMPLANTOLOGY-001
    - Descripción: El tiempo de cicatrización debe estar entre 2 y 12 meses.
    - [ADR-XX-ERR_IMPLANTOLOGY_INVALID_HEALING_TIME.md](ADR-XX-ERR_IMPLANTOLOGY_INVALID_HEALING_TIME.md)

2. **ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH**
    - Regla: RN-IMPLANTOLOGY-002
    - Descripción: Con injerto óseo, el tiempo de cicatrización mínimo es 4 meses.
    - [ADR-XX-ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH.md](ADR-XX-ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH.md)

3. **ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME**
    - Regla: RN-IMPLANTOLOGY-003
    - Descripción: El tiempo de cicatrización no puede ser negativo.
    - [ADR-XX-ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME.md](ADR-XX-ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME.md)

---

#### Advertencias sobre tiempos atípicos
4. **WARN_IMPLANTOLOGY_SHORT_HEALING_TIME**
    - Regla: RN-IMPLANTOLOGY-004
    - Descripción: Tiempos menores a 3 meses sin injerto son atípicos.
    - [ADR-XX-WARN_IMPLANTOLOGY_SHORT_HEALING_TIME.md](ADR-XX-WARN_IMPLANTOLOGY_SHORT_HEALING_TIME.md)

5. **WARN_IMPLANTOLOGY_LONG_HEALING_TIME**
    - Regla: RN-IMPLANTOLOGY-005
    - Descripción: Tiempos mayores a 9 meses sin injerto complejo son atípicos.
    - [ADR-XX-WARN_IMPLANTOLOGY_LONG_HEALING_TIME.md](ADR-XX-WARN_IMPLANTOLOGY_LONG_HEALING_TIME.md)

6. **WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING**
    - Regla: RN-IMPLANTOLOGY-006
    - Descripción: Implantes zigomáticos requieren tiempo de cicatrización extendido (6+ meses).
    - [ADR-XX-WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING.md](ADR-XX-WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING.md)

---

#### Validaciones de sitio de colocación
7. **ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE**
    - Regla: RN-IMPLANTOLOGY-007
    - Descripción: El sitio de colocación debe tener formato válido si se especifica.
    - [ADR-XX-ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE.md](ADR-XX-ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE.md)

---
