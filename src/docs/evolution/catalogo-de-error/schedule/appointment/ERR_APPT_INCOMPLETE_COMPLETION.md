## ERR_APPT_INCOMPLETE_COMPLETION

- **Código:** ERR_APPT_INCOMPLETE_COMPLETION
- **Nombre corto:** Finalización incompleta
- **Mensaje base:** "Solo puede finalizarse si tiene duración real y notas clínicas"
- **Descripción clínica:**  
  Impide cerrar una cita sin datos completos de duración y notas clínicas. Protege la calidad del registro médico.
- **Operación / Caso de uso:** FINALIZAR_CITA (completeAppointment)
- **Regla de negocio:** RN-APPT-005 — Restricción de finalización incompleta (pospuesta, ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Cita ID 456 intentó finalizarse sin notas clínicas"
- **Mapa a código existente:** Nuevo código (pospuesto)
- **Justificación ética:** Garantiza que los registros clínicos sean completos y útiles para auditoría y seguimiento.
- **Ejemplo de uso:**
  ```java
  if (duration == null || notes.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_INCOMPLETE_COMPLETION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita sin notas → excepción.
    - **Integración:** PUT /appointments/{id}/complete → 400 si incompleta.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment (pospuesto).

---