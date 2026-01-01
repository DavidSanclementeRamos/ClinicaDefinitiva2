## ERR_APPOINTMENT_ID_REQUIRED

- **Código:** ERR_APPOINTMENT_ID_REQUIRED
- **Nombre corto:** AppointmentId requerido
- **Mensaje base:** "El valor de AppointmentId no puede ser nulo"
- **Descripción clínica:**  
  Impide crear o manipular citas sin un identificador válido. Protege la trazabilidad clínica y evita registros huérfanos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-001 — Identificador obligatorio (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "AppointmentId nulo en creación de cita"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que cada cita sea única y rastreable en el sistema clínico.
- **Ejemplo de uso:**
  ```java
  if (appointmentId == null) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_APPOINTMENT_ID_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** appointmentId = null → excepción.
    - **Integración:** POST /appointments → 400 si falta ID.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Appointment.

---
