## ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH

- **Código:** ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH
- **Nombre corto:** Duración insuficiente con quirófano
- **Mensaje base:** "Si requiere quirófano, la duración del servicio debe ser al menos 60 minutos"
- **Descripción clínica:**  
  Valida que las intervenciones que solicitan quirófano incluyan una duración mínima razonable (≥ 60 minutos) para cubrir preparación, intervención y recuperación inicial, evitando programaciones inviables.
- **Operación Caso de uso:** planSurgery
- **Regla de negocio:** RN-SURGICAL-002 — Duración mínima para cirugías con quirófano (ver ADR-101)
- **Contexto del agregado:** SURGERY
- **Tipo semántico:** Validación de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "surgeryId=456 requiresOperatingRoom=true durationMinutes=45"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita planificaciones que pongan en riesgo la seguridad por falta de tiempo para procedimientos y cuidados inmediatos.
- **Ejemplo de uso:**
  ```java
  if (surgery.requiresOperatingRoom() && surgery.getDurationMinutes() < 60) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** requiresOperatingRoom=true y duration=45 → excepción.
    - **Integración:** POST /surgeries con quirófano y duration < 60 → 422.
- **Changelog versión:** 2026-01-08, David — Alta inicial catálogo Surgical.

---