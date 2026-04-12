## ERR_PATIENT_NO_SHIFT_ASSIGNED

- **Código:** ERR_PATIENT_NO_SHIFT_ASSIGNED
- **Nombre corto:** Paciente sin turno asignado
- **Mensaje base:** "El paciente no tiene un turno asignado"
- **Descripción clínica:**  
  Valida que todo paciente tenga al menos un turno asignado en el sistema. Evita que existan pacientes registrados sin agenda clínica, lo que afectaría la trazabilidad y la atención médica.
- **Operación / Caso de uso:** VALIDAR_TURNO_PACIENTE (validatePatientShift)
- **Regla de negocio:** RN-PATIENT-011 — Paciente debe tener turno asignado (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Paciente ID 67 sin turno asignado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes tengan acceso a la atención médica y evita registros incompletos.
- **Ejemplo de uso:**
  ```java
  if (patient.assignedShift == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_NO_SHIFT_ASSIGNED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** paciente sin turno → excepción.
    - **Integración:** GET /patients/{id}/shift → 400 si no existe turno.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---
