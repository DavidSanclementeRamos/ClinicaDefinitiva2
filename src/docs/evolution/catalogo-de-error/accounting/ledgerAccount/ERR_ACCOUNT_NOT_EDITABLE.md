# ERR_ACCOUNT_NOT_EDITABLE
- **Código:** ERR_ACCOUNT_NOT_EDITABLE
- **Nombre corto:** Cuenta no editable
- **Mensaje base:** `error.ledgerAccount.notEditable` — "La cuenta solo puede editarse si está activa"
- **Descripción clínica:** Se lanza cuando se intenta modificar una cuenta inactiva.
- **Operación / Caso de uso:** UPDATE_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-004
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** `"Intento de edición en cuenta ID 456 inactiva"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("No editable")`
- **Justificación ética:** Protege la coherencia del estado de las cuentas.
- **Ejemplo de uso:**
  ```java
  if (!active) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (editar cuenta inactiva), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---