## ERR_APPOINTMENT_STATUS_INVALID_TRANSITION

- **Código:** ERR_APPOINTMENT_STATUS_INVALID_TRANSITION
- **Nombre corto:** Transición de estado inválida
- **Mensaje base:** "No se puede transicionar desde el estado actual a un estado inválido"
- **Descripción clínica:**  
  Impide cambios de estado no permitidos en citas (ej. COMPLETED → SCHEDULED). Protege la coherencia clínica y evita inconsistencias operativas.
- **Operación / Caso de uso:** ACTUALIZAR_ESTADO_CITA (updateAppointmentStatus)
- **Regla de negocio:** RN-APPT-002 — Restricción de transición inválida (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Transición inválida: COMPLETED → SCHEDULED"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita manipulación indebida de registros clínicos y protege la trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (!status.canTransitionTo(newStatus)) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_APPOINTMENT_STATUS_INVALID_TRANSITION);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** transición inválida → excepción.
    - **Integración:** PUT /appointments/{id}/status → 409 si transición inválida.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Appointment.

---
