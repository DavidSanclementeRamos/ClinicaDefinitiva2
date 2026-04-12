## ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON

- **Código:** ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON
- **Nombre corto:** Desactivación requiere motivo obligatorio
- **Mensaje base:** "La desactivación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a que toda desactivación de un responsable esté acompañada de un motivo explícito. Esto asegura trazabilidad administrativa y permite auditar las razones de baja de responsables clínicos.
- **Operación / Caso de uso:** DESACTIVAR_RESPONSABLE (deactivateGuardian)
- **Regla de negocio:** RN-GUARDIAN-010 — Motivo obligatorio en desactivación (ver ADR-21)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Sistema
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Guardian ID 54 desactivado sin motivo registrado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en la gestión clínica y protege a los pacientes de bajas injustificadas de responsables.
- **Ejemplo de uso:**
  ```java
  if (deactivationReason == null || deactivationReason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** desactivación sin motivo → excepción.
    - **Integración:** PUT /guardians/{id}/deactivate sin motivo → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---