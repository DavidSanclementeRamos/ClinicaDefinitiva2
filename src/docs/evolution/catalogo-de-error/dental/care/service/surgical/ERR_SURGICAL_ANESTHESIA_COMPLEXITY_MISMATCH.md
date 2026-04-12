## ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH

- **Código:** ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH
- **Nombre corto:** Complejidad insuficiente con anestesia
- **Mensaje base:** "Si requiere anestesia, el nivel de complejidad debe ser al menos MEDIUM"
- **Descripción clínica:**  
  Garantiza que los procedimientos que requieren anestesia general o regional estén clasificados con un nivel de complejidad acorde (MEDIUM o superior), evitando subestimaciones que comprometan la seguridad y la asignación de recursos anestésicos.
- **Operación Caso de uso:** planSurgery
- **Regla de negocio:** RN-SURGICAL-001 — Validación de complejidad cuando se requiere anestesia (ver ADR-100)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Integridad clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "surgeryId=789 requiresAnesthesia=true complexity=LOW"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente asegurando que la clasificación refleje los riesgos reales y los recursos necesarios para una atención segura.
- **Ejemplo de uso:**
  ```java
  if (surgery.requiresAnesthesia() && surgery.getComplexity().isLowerThan(Complexity.MEDIUM)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** requiresAnesthesia=true y complexity=LOW → excepción.
    - **Integración:** POST /surgeries con requiresAnesthesia=true y complexity=LOW → 422.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.

---
