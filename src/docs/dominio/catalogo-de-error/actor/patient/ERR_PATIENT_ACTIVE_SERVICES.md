## ERR_PATIENT_ACTIVE_SERVICES

- **Código:** ERR_PATIENT_ACTIVE_SERVICES
- **Nombre corto:** Desactivación con servicios activos
- **Mensaje base:** "No puede desactivarse si tiene citas activas o tratamientos en curso"
- **Descripción clínica:**  
  Impide que un paciente sea desactivado mientras mantiene citas o tratamientos clínicos activos. Protege la continuidad de la atención y evita pérdida de trazabilidad en procesos médicos en curso.
- **Operación / Caso de uso:** DESACTIVAR_PACIENTE (deactivatePatient)
- **Regla de negocio:** RN-PATIENT-002 — Restricción de desactivación con servicios activos (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Paciente ID 101 con tratamientos activos no puede desactivarse"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes reciban continuidad en su atención y evita cancelaciones abruptas de servicios médicos.
- **Ejemplo de uso:**
  ```java
  if (patient.hasActiveAppointmentsOrTreatments()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_ACTIVE_SERVICES);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** paciente con citas activas → excepción.
    - **Integración:** PUT /patients/{id}/deactivate con servicios activos → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---
