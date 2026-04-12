### ERR_ACCOUNT_DUPLICATE_CODE
- **Código:** ERR_ACCOUNT_DUPLICATE_CODE
- **Nombre corto:** Código duplicado
- **Mensaje base:** `error.ledgerAccount.duplicateCode` — "El código de la cuenta debe ser único por compañía"
- **Descripción clínica:** Se lanza cuando ya existe otra cuenta con el mismo código dentro de la misma compañía.
- **Operación / Caso de uso:** CREAR_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-009
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Cuenta con código '1105' ya existe en compañía 001"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("Código duplicado")`
- **Justificación ética:** Evita duplicidad de cuentas que afecten la consistencia del plan contable.
- **Ejemplo de uso:**
  ```java
  if (repository.existsByCodeAndCompanyId(code, companyId)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_DUPLICATE_CODE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (código duplicado), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---