## ERR_DENTIST_OUT_OF_WORKING_HOURS

- **Código:** ERR_DENTIST_OUT_OF_WORKING_HOURS
- **Nombre corto:** Cita fuera de horario laboral
- **Mensaje base:** "El horario solicitado está fuera de las horas laborales declaradas"
- **Descripción clínica:**  
  Impide que se agenden citas en horarios no declarados como laborales por el odontólogo. Protege la organización de la clínica y evita comprometer al profesional fuera de sus condiciones de trabajo.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-DENTIST-011 — Restricción de horario laboral (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de agendar cita a las 22:00 fuera de horario laboral odontólogo ID 78"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege el bienestar del profesional y asegura que los pacientes reciban atención en condiciones adecuadas.
- **Ejemplo de uso:**
  ```java
  if (!dentist.workingHours.includes(requestedTime)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_OUT_OF_WORKING_HOURS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita fuera de horario laboral → excepción.
    - **Integración:** POST /appointments con hora 22:00 → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---