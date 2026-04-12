## ERR_APPT_LATE_CANCELLATION

- **Código:** ERR_APPT_LATE_CANCELLATION
- **Nombre corto:** Cancelación tardía
- **Mensaje base:** "No puede cancelarse dentro de las 24h previas"
- **Descripción clínica:**  
  Impide que se cancelen citas en menos de 24 horas de anticipación. Protege la estabilidad operativa y evita perjuicios a pacientes y odontólogos.
- **Operación / Caso de uso:** CANCELAR_CITA (cancelAppointment)
- **Regla de negocio:** RN-APPT-007 — Restricción de cancelación tardía (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 403
- **Detalle dinámico sugerido:** "Cita ID 789 cancelada dentro de 24h sin autorización"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes reciban atención sin cancelaciones abruptas.
- **Ejemplo de uso:**
  ```java
  if (appointment.isWithin24h()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_LATE_CANCELLATION);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita dentro de 24h → excepción.
    - **Integración:** DELETE /appointments/{id} → 403 si cancelación tardía.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---