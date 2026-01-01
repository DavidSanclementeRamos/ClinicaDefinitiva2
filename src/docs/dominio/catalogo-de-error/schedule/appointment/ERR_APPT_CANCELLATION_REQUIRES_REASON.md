## ERR_APPT_CANCELLATION_REQUIRES_REASON

- **Código:** ERR_APPT_CANCELLATION_REQUIRES_REASON
- **Nombre corto:** Cancelación sin motivo
- **Mensaje base:** "La cancelación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a registrar un motivo clínico o administrativo al cancelar una cita. Protege la trazabilidad y permite auditoría de decisiones.
- **Operación / Caso de uso:** CANCELAR_CITA (cancelAppointment)
- **Regla de negocio:** RN-APPT-008 — Cancelación requiere motivo (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Cita ID 321 cancelada sin motivo registrado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en cancelaciones y protege la confianza clínica.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_CANCELLATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cancelación sin motivo → excepción.
    - **Integración:** DELETE /appointments/{id} → 400 si falta motivo.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---