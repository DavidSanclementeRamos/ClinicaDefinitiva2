## ERR_SHIFT_CANCELLATION_REQUIRES_REASON

- **Código:** ERR_SHIFT_CANCELLATION_REQUIRES_REASON
- **Nombre corto:** Cancelación sin motivo
- **Mensaje base:** "La cancelación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a registrar un motivo clínico o administrativo al cancelar un turno. Protege la trazabilidad y permite auditoría.
- **Operación / Caso de uso:** CANCELAR_TURNO (cancelShift)
- **Regla de negocio:** RN-SHIFT-007 — Cancelación requiere motivo (ver ADR-24)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno ID 123 cancelado sin motivo"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en la gestión de turnos y protege la confianza clínica.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_CANCELLATION_REQUIRES_REASON);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** cancelación sin motivo → excepción.
    - **Integración:** DELETE /shifts/{id} → 400 si falta motivo.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---