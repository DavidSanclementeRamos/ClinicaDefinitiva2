## ERR_PATIENT_DEACTIVATION_REQUIRES_REASON

- **Código:** ERR_PATIENT_DEACTIVATION_REQUIRES_REASON
- **Nombre corto:** Desactivación paciente requiere motivo
- **Mensaje base:** "La desactivación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a que toda desactivación de un paciente esté acompañada de un motivo explícito. Esto asegura trazabilidad administrativa y permite auditar las razones de baja de pacientes en el sistema clínico.
- **Operación / Caso de uso:** DESACTIVAR_PACIENTE (deactivatePatient)
- **Regla de negocio:** RN-PATIENT-010 — Motivo obligatorio en desactivación (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Sistema
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Paciente ID 45 desactivado sin motivo registrado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en la gestión clínica y protege a los pacientes de bajas injustificadas.
- **Ejemplo de uso:**
  ```java
  if (deactivationReason == null || deactivationReason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_DEACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** desactivación sin motivo → excepción.
    - **Integración:** PUT /patients/{id}/deactivate sin motivo → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---