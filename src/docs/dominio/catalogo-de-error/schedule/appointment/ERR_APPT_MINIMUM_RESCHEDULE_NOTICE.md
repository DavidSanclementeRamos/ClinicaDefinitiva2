### ERR_APPT_MINIMUM_RESCHEDULE_NOTICE

- **Código:** ERR_APPT_MINIMUM_RESCHEDULE_NOTICE
- **Nombre corto:** Reagendamiento sin anticipación mínima
- **Mensaje base:** "No se puede reagendar con menos de 24 horas de anticipación"
- **Descripción clínica:**  
  Impide reagendar citas cuando faltan menos de 24 horas para su realización. Protege la estabilidad de la agenda clínica y evita perjuicios a pacientes y profesionales por cambios tardíos.
- **Operación / Caso de uso:** REAGENDAR_CITA (rescheduleAppointment)
- **Regla de negocio:** RN-APPT-012 — Anticipación mínima para reagendar (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Cita ID 512 intenta reagendarse a T+12h (mínimo requerido: 24h)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza previsibilidad en la atención y reduce cancelaciones/retrabajos por cambios intempestivos.
- **Ejemplo de uso:**
  ```java
  if (!appointment.hasMinimumRescheduleNotice(Duration.ofHours(24))) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_MINIMUM_RESCHEDULE_NOTICE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** reagendamiento con anticipación < 24h → excepción.
    - **Integración:** PUT /appointments/{id}/reschedule → 409 si anticipación insuficiente.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---
