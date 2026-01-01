## ERR_APPT_PATIENT_TIME_CONFLICT

- **Código:** ERR_APPT_PATIENT_TIME_CONFLICT
- **Nombre corto:** Conflicto de horario paciente
- **Mensaje base:** "No puede haber dos citas en el mismo horario para el mismo paciente"
- **Descripción clínica:**  
  Evita que un paciente tenga dos citas simultáneas. Protege la coherencia clínica y evita errores administrativos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-009 — Restricción de duplicidad de citas paciente (ver ADR-25)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 123 ya tiene cita en el horario solicitado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita sobrecarga de pacientes y garantiza atención ordenada.
- **Ejemplo de uso:**
  ```java
  if (patient.hasAppointmentAt(slot)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_PATIENT_TIME_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** paciente con cita en mismo horario → excepción.
    - **Integración:** POST /appointments → 409 si conflicto.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---