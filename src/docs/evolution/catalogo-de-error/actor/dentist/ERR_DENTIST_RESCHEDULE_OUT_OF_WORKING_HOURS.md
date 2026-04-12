## ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS

- **Código:** ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS
- **Nombre corto:** Reagendación fuera de horario laboral
- **Mensaje base:** "La reagendación está fuera de las horas laborales del odontólogo"
- **Descripción clínica:**  
  Impide que una cita ya existente sea reagendada a un horario que no corresponde con las horas laborales declaradas por el odontólogo. Protege la organización de la clínica y evita comprometer al profesional en horarios no autorizados.
- **Operación / Caso de uso:** REAGENDAR_CITA (rescheduleAppointment)
- **Regla de negocio:** RN-DENTIST-014 — Restricción de reagendación fuera de horario laboral (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de reagendar cita a las 21:30 fuera de horario laboral odontólogo ID 112"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes reciban atención en condiciones adecuadas y protege el bienestar del profesional.
- **Ejemplo de uso:**
  ```java
  if (!dentist.workingHours.includes(newAppointmentTime)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_RESCHEDULE_OUT_OF_WORKING_HOURS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** reagendación fuera de horario laboral → excepción.
    - **Integración:** PUT /appointments/{id}/reschedule con hora 21:30 → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---