## ERR_APPT_PAST_DATE

- **Código:** ERR_APPT_PAST_DATE
- **Nombre corto:** Cita en fecha pasada
- **Mensaje base:** "La fecha/hora de la cita no puede estar en el pasado"
- **Descripción clínica:**  
  Impide que se registren citas en fechas ya transcurridas. Protege la coherencia temporal y evita registros inválidos.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-010 — Restricción de fecha pasada (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Intento de agendar cita en fecha pasada: 2025-01-01"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias temporales y protege la integridad del sistema clínico.
- **Ejemplo de uso:**
  ```java
  if (appointmentDate.isBefore(now)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_PAST_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita en fecha pasada → excepción.
    - **Integración:** POST /appointments → 400 si fecha pasada.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.

---