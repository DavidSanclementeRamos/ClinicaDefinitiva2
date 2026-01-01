## ERR_APPT_DENTIST_TIME_CONFLICT

- **Código:** ERR_APPT_DENTIST_TIME_CONFLICT
- **Nombre corto:** Conflicto de horario odontólogo
- **Mensaje base:** "No puede haber dos citas en el mismo horario para el mismo odontólogo"
- **Descripción clínica:**  
  Evita que un odontólogo tenga dos citas simultáneas. Protege la calidad de atención y evita sobrecarga operativa.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-004 — Restricción de duplicidad de citas odontólogo (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Odontólogo ID 87 ya tiene cita en el horario solicitado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que cada paciente reciba atención exclusiva y evita errores administrativos.
- **Ejemplo de uso:**
  ```java
  if (dentist.hasAppointmentAt(slot)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_DENTIST_TIME_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo con cita en mismo horario → excepción.
    - **Integración:** POST /appointments → 409 si conflicto.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---