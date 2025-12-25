## ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON

- **Código:** ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON
- **Nombre corto:** Desactivación recepcionista requiere motivo
- **Mensaje base:** "La desactivación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a que toda desactivación de un recepcionista esté acompañada de un motivo explícito. Esto asegura trazabilidad administrativa y permite auditar las razones de baja de personal administrativo en la clínica.
- **Operación / Caso de uso:** DESACTIVAR_RECEPCIONISTA (deactivateReceptionist)
- **Regla de negocio:** RN-RECEPTIONIST-010 — Motivo obligatorio en desactivación (ver ADR-23)
- **Contexto del agregado:** RECEPCIONISTA
- **Tipo semántico:** Sistema
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Recepcionista ID 92 desactivado sin motivo registrado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en la gestión administrativa y protege la clínica de bajas injustificadas de personal clave.
- **Ejemplo de uso:**
  ```java
  if (deactivationReason == null || deactivationReason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** desactivación sin motivo → excepción.
    - **Integración:** PUT /receptionists/{id}/deactivate sin motivo → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Receptionist.

---