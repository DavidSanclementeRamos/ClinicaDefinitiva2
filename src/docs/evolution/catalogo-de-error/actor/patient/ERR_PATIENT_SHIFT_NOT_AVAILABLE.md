## ERR_PATIENT_SHIFT_NOT_AVAILABLE

- **Código:** ERR_PATIENT_SHIFT_NOT_AVAILABLE
- **Nombre corto:** Turno paciente no disponible
- **Mensaje base:** "El horario solicitado no está dentro del turno asignado al paciente"
- **Descripción clínica:**  
  Impide que se agenden citas en horarios fuera del turno asignado al paciente. Protege la coherencia de la agenda clínica y evita asignaciones inválidas.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-PATIENT-012 — Validación de turno paciente (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 89 intentó agendar cita fuera de turno asignado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes reciban atención en horarios válidos y evita inconsistencias en la planificación clínica.
- **Ejemplo de uso:**
  ```java
  if (!patient.assignedShift.includes(requestedTime)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_SHIFT_NOT_AVAILABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** cita fuera de turno → excepción.
    - **Integración:** POST /appointments con horario fuera de turno → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---