### ERR_ACCOUNT_ALREADY_ACTIVE
- **Código:** ERR_ACCOUNT_ALREADY_ACTIVE
- **Nombre corto:** Cuenta ya activa
- **Mensaje base:** `error.ledgerAccount.alreadyActive` — "La cuenta ya está activa"
- **Descripción clínica:** Se lanza cuando se intenta activar una cuenta que ya se encuentra activa.
- **Operación / Caso de uso:** ACTIVATE_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-010
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** `"Intento de activar cuenta ID 789 ya activa"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("La cuenta ya está activa")`
- **Justificación ética:** Evita operaciones redundantes y protege la coherencia del estado de las cuentas.
- **Ejemplo de uso:**
  ```java
  if (this.active) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_ALREADY_ACTIVE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (activar cuenta activa), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---