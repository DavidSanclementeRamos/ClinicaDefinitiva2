### ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON
- **Código:** ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON
- **Nombre corto:** Inactivación requiere motivo
- **Mensaje base:** `error.ledgerAccount.inactivationRequiresReason` — "La inactivación de la cuenta requiere un motivo obligatorio"
- **Descripción clínica:** Se lanza cuando se intenta inactivar una cuenta sin motivo.
- **Operación / Caso de uso:** INACTIVATE_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-005
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Intento de inactivación en cuenta ID 789 sin motivo"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("Motivo obligatorio")`
- **Justificación ética:** Garantiza trazabilidad y responsabilidad en cambios de estado.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (motivo nulo), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---