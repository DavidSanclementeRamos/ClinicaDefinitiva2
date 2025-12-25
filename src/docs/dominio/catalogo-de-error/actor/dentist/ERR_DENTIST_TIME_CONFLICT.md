## ERR_DENTIST_TIME_CONFLICT

- **Código:** ERR_DENTIST_TIME_CONFLICT
- **Nombre corto:** Conflicto de horario odontólogo
- **Mensaje base:** "El odontólogo ya tiene una cita agendada en este horario"
- **Descripción clínica:**  
  Evita que se agenden citas en horarios donde el odontólogo ya tiene compromisos registrados. Protege la organización de la agenda y la confianza del paciente.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-DENTIST-004 — Evitar conflictos de horario (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de agendar cita en horario ocupado por odontólogo ID 102"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes no reciban citas duplicadas y que el odontólogo no sea sobrecargado.
- **Ejemplo de uso:**
  ```java
  if (dentist.schedule.conflictsWith(newAppointment)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_TIME_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita en horario ocupado → excepción.
    - **Integración:** POST /appointments con horario duplicado → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---