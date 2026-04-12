## ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS

- **Código:** ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS
- **Nombre corto:** Requisitos faltantes en CRITICAL
- **Mensaje base:** "Cirugías CRITICAL deben requerir anestesia y quirófano"
- **Descripción clínica:**  
  Garantiza que las cirugías clasificadas como CRITICAL incluyan obligatoriamente anestesia y quirófano, condiciones mínimas para la seguridad del paciente en intervenciones de alto riesgo.
- **Operación Caso de uso:** planSurgery
- **Regla de negocio:** RN-SURGICAL-004 — Requisitos obligatorios para complejidad CRITICAL (ver ADR-103)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Integridad clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "surgeryId=999 complexity=CRITICAL requiresAnesthesia=false requiresOperatingRoom=false"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la vida del paciente asegurando condiciones mínimas y recursos necesarios para cirugías críticas.
- **Ejemplo de uso:**
  ```java
  if (surgery.getComplexity() == Complexity.CRITICAL &&
      (!surgery.requiresAnesthesia() || !surgery.requiresOperatingRoom())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** complexity=CRITICAL sin anestesia → excepción.
    - **Integración:** POST /surgeries CRITICAL sin quirófano → 422.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.

---
