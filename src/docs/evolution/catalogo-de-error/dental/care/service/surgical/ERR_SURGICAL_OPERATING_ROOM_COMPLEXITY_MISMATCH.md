## ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH

- **Código:** ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH
- **Nombre corto:** Complejidad insuficiente con quirófano
- **Mensaje base:** "Cirugías que requieren quirófano deben tener complejidad al menos MEDIUM"
- **Descripción clínica:**  
  Asegura coherencia entre la solicitud de quirófano y la clasificación de complejidad; solicitar quirófano para procedimientos LOW puede indicar error en la clasificación o en la solicitud de recursos.
- **Operación Caso de uso:** planSurgery
- **Regla de negocio:** RN-SURGICAL-007 — Validación de complejidad para uso de quirófano (ver ADR-106)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Integridad de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "surgeryId=444 requiresOperatingRoom=true complexity=LOW"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita asignación incorrecta de recursos críticos y protege al paciente frente a planificaciones inconsistentes.
- **Ejemplo de uso:**
  ```java
  if (surgery.requiresOperatingRoom() && surgery.getComplexity().isLowerThan(Complexity.MEDIUM)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** requiresOperatingRoom=true y complexity=LOW → excepción.
    - **Integración:** POST /surgeries con quirófano solicitado y complexity=LOW → 422.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.
---




