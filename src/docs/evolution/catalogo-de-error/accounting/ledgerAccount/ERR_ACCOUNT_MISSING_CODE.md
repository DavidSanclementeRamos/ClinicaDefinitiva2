###  ERR_ACCOUNT_MISSING_CODE
- **Código:** ERR_ACCOUNT_MISSING_CODE
- **Nombre corto:** Código obligatorio
- **Mensaje base:** `error.ledgerAccount.missingCode` — "El código de la cuenta es obligatorio"
- **Descripción clínica:** Se lanza cuando se intenta crear una cuenta sin código.
- **Operación / Caso de uso:** CREAR_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-011
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Intento de crear cuenta sin código"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("El código de la cuenta es obligatorio")`
- **Justificación ética:** Evita cuentas huérfanas sin identificador válido.
- **Ejemplo de uso:**
  ```java
  if (code == null || code.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_MISSING_CODE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (código nulo o vacío), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---