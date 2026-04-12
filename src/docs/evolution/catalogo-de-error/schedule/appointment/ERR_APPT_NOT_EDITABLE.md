## ERR_APPT_NOT_EDITABLE

- **Código:** ERR_APPT_NOT_EDITABLE
- **Nombre corto:** Cita no editable
- **Mensaje base:** "Solo puede editarse si está en estado SCHEDULED o CONFIRMED"
- **Descripción clínica:**  
  Impide modificar citas que ya fueron completadas, canceladas o están fuera de estado válido. Protege la trazabilidad clínica y evita alteraciones indebidas en registros históricos.
- **Operación / Caso de uso:** EDITAR_CITA (updateAppointment)
- **Regla de negocio:** RN-APPT-006 — Restricción de edición según estado (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Cita ID 456 no editable en estado COMPLETED"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita manipulación de registros clínicos cerrados y protege la historia médica.
- **Ejemplo de uso:**
  ```java
  if (!appointment.isEditable()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita en estado COMPLETED → excepción.
    - **Integración:** PUT /appointments/{id} → 409 si no editable.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---
