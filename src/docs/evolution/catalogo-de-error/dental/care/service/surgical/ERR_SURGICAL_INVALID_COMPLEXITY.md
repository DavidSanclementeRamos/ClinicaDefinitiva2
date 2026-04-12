## ERR_SURGICAL_INVALID_COMPLEXITY

- **Código:** ERR_SURGICAL_INVALID_COMPLEXITY
- **Nombre corto:** Complejidad inválida
- **Mensaje base:** "El nivel de complejidad debe ser: LOW, MEDIUM, HIGH o CRITICAL"
- **Descripción clínica:**  
  Asegura que el campo de complejidad solo acepte valores del dominio permitido, evitando clasificaciones ambiguas que afecten planificación, recursos y comunicación clínica.
- **Operación Caso de uso:** createOrUpdateSurgery
- **Regla de negocio:** RN-SURGICAL-003 — Validación de catálogo de complejidad (ver ADR-102)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "surgeryId=111 complexity='MINOR' no permitido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores en la categorización del riesgo y asegura que los equipos asignen recursos adecuados.
- **Ejemplo de uso:**
  ```java
  if (!EnumSet.of(Complexity.LOW, Complexity.MEDIUM, Complexity.HIGH, Complexity.CRITICAL).contains(surgery.getComplexity())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SURGICAL_INVALID_COMPLEXITY);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** complexity="MINOR" → excepción.
    - **Integración:** POST /surgeries con complexity inválida → 422.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.

---
