## ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT

- **Código:** ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT
- **Nombre corto:** Cita duplicada paciente
- **Mensaje base:** "No puede agendar citas duplicadas para el mismo paciente en el mismo horario"
- **Descripción clínica:**  
  Evita que un recepcionista agende dos citas para el mismo paciente en el mismo horario. Protege la organización de la clínica y evita conflictos en la agenda.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-RECEPTIONIST-002 — Restricción de citas duplicadas (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 78 ya tiene cita en horario 2025-12-25 11:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes no reciban citas duplicadas y que la clínica mantenga orden en la agenda.
- **Ejemplo de uso:**
  ```java
  if (appointmentRepository.exists(patientId, requestedTime)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita duplicada → excepción.
    - **Integración:** POST /appointments con paciente ya agendado → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---