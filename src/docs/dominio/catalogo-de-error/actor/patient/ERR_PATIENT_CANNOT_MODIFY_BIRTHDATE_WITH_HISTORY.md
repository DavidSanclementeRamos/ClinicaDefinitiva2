## ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY

- **Código:** ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY
- **Nombre corto:** Fecha de nacimiento inmutable con historial
- **Mensaje base:** "No se puede modificar la fecha de nacimiento si el paciente tiene historial de citas"
- **Descripción clínica:**  
  Impide que la fecha de nacimiento de un paciente sea modificada una vez que ya existe historial clínico o de citas. Protege la trazabilidad de la información médica y evita fraudes o inconsistencias en registros.
- **Operación / Caso de uso:** ACTUALIZAR_DATOS_PACIENTE (updatePatientData)
- **Regla de negocio:** RN-PATIENT-009 — Fecha de nacimiento inmutable con historial (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 78 intentó modificar fecha de nacimiento con historial clínico existente"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza la veracidad de la información demográfica y protege la integridad de los registros clínicos.
- **Ejemplo de uso:**
  ```java
  if (patient.hasHistory() && birthdateChanged) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE_WITH_HISTORY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** paciente con historial y cambio de fecha → excepción.
    - **Integración:** PUT /patients/{id}/birthdate con historial → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---
