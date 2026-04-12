## WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM

- **Código:** WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM
- **Nombre corto:** Quirófano en baja complejidad
- **Mensaje base:** "Cirugías de baja complejidad no suelen requerir quirófano"
- **Descripción clínica:**  
  Advierte cuando se solicita quirófano para procedimientos catalogados como LOW, sugiriendo revisión clínica para confirmar la necesidad y evitar uso innecesario de recursos.
- **Operación Caso de uso:** planSurgery
- **Regla de negocio:** RN-SURGICAL-005 — Advertencia sobre quirófano en baja complejidad (ver ADR-104)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "surgeryId=222 complexity=LOW requiresOperatingRoom=true"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita uso innecesario de quirófano y protege al paciente de intervenciones excesivas o logísticas innecesarias.
- **Ejemplo de uso:**
  ```java
  if (surgery.getComplexity() == Complexity.LOW && surgery.requiresOperatingRoom()) {
      log.warn(ErrorCatalog.WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** complexity=LOW y requiresOperatingRoom=true → warning registrado.
    - **Integración:** POST /surgeries con LOW y quirófano → 200 con warning en logs.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.

---
