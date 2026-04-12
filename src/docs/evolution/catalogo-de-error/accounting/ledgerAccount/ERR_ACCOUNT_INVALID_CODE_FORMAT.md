# ERR_ACCOUNT_INVALID_CODE_FORMAT
- **Código:** ERR_ACCOUNT_INVALID_CODE_FORMAT
- **Nombre corto:** Formato de código inválido
- **Mensaje base:** `error.ledgerAccount.invalidCodeFormat` — "El código de la cuenta solo puede contener dígitos numéricos"
- **Descripción clínica:** Se lanza cuando el código contiene caracteres no numéricos.
- **Operación / Caso de uso:** CREAR_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-002
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Cuenta con código 'AB12' inválido: contiene caracteres no numéricos"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("Formato inválido")`
- **Justificación ética:** Garantiza que los códigos sean auditables y consistentes.
- **Ejemplo de uso:**
  ```java
  if (!code.matches("[0-9]{1,8}")) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_INVALID_CODE_FORMAT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (código con letras), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---