# ERR_ACCOUNT_MISSING_NATURE
- **Código:** ERR_ACCOUNT_MISSING_NATURE
- **Nombre corto:** Naturaleza obligatoria
- **Mensaje base:** `error.ledgerAccount.missingNature` — "La naturaleza de la cuenta es obligatoria"
- **Descripción clínica:** Se lanza cuando se intenta crear una cuenta sin naturaleza definida (activo, pasivo, etc.).
- **Operación / Caso de uso:** CREAR_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-003
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Cuenta ID 123 sin naturaleza definida"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("Naturaleza obligatoria")`
- **Justificación ética:** Evita cuentas sin clasificación contable válida.
- **Ejemplo de uso:**
  ```java
  if (nature == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_MISSING_NATURE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (naturaleza nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---