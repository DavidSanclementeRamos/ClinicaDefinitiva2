## ERR_APPOINTMENT_STATUS_REQUIRED

- **Código:** ERR_APPOINTMENT_STATUS_REQUIRED
- **Nombre corto:** Estado de cita requerido
- **Mensaje base:** "El estado de Appointment no puede ser nulo"
- **Descripción clínica:**  
  Impide que una cita exista sin estado definido. Protege la coherencia operativa y evita registros ambiguos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-001 — Estado obligatorio (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Appointment sin estado asignado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que cada cita tenga un estado claro y verificable.
- **Ejemplo de uso:**
  ```java
  if (status == null) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_APPOINTMENT_STATUS_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** status = null → excepción.
    - **Integración:** POST /appointments → 400 si falta estado.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Appointment.

---
