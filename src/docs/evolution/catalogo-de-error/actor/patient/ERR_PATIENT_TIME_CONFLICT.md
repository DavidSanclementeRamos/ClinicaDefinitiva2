## ERR_PATIENT_TIME_CONFLICT

- **Código:** ERR_PATIENT_TIME_CONFLICT
- **Nombre corto:** Conflicto de horario paciente
- **Mensaje base:** "El paciente ya tiene una cita agendada en este horario"
- **Descripción clínica:**  
  Evita que un paciente reciba citas duplicadas en el mismo horario. Protege la organización de la agenda clínica y evita sobrecarga o solapamiento de servicios.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-PATIENT-003 — Validación de conflicto de horario paciente (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 202 ya tiene cita en horario 2025-12-25 10:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes no reciban citas duplicadas y que la clínica mantenga orden en la agenda.
- **Ejemplo de uso:**
  ```java
  if (patient.schedule.conflictsWith(newAppointment)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_TIME_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita duplicada → excepción.
    - **Integración:** POST /appointments con paciente ya agendado → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---
