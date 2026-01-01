## ERR_APPT_OUTSIDE_AVAILABILITY

- **Código:** ERR_APPT_OUTSIDE_AVAILABILITY
- **Nombre corto:** Cita fuera de disponibilidad
- **Mensaje base:** "No puede agendarse fuera del horario de disponibilidad"
- **Descripción clínica:**  
  Impide que se agenden citas en horarios no declarados por el odontólogo. Protege la coherencia operativa y evita conflictos de agenda.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-002 — Restricción de agendamiento fuera de disponibilidad (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 123 intentó agendar fuera de disponibilidad del odontólogo ID 87"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que las citas respeten la disponibilidad declarada y evita sobrecarga de profesionales.
- **Ejemplo de uso:**
  ```java
  if (!availability.contains(requestedSlot)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_OUTSIDE_AVAILABILITY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita fuera de disponibilidad → excepción.
    - **Integración:** POST /appointments → 409 si fuera de disponibilidad.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---