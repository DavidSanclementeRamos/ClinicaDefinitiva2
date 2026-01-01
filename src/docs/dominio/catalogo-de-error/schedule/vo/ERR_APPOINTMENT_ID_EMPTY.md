## ERR_APPOINTMENT_ID_EMPTY

- **Código:** ERR_APPOINTMENT_ID_EMPTY
- **Nombre corto:** AppointmentId vacío
- **Mensaje base:** "El valor de AppointmentId no puede estar vacío"
- **Descripción clínica:**  
  Impide que el identificador de cita sea una cadena vacía. Protege la integridad de datos y evita registros inválidos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-002 — Identificador no vacío (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "AppointmentId vacío en creación de cita"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias en la trazabilidad clínica.
- **Ejemplo de uso:**
  ```java
  if (appointmentId.trim().isEmpty()) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_APPOINTMENT_ID_EMPTY);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** appointmentId = "" → excepción.
    - **Integración:** POST /appointments → 400 si ID vacío.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Appointment.

---