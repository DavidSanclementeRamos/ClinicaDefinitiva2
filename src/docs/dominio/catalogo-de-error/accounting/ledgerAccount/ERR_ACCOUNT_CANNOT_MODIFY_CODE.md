# ERR_ACCOUNT_CANNOT_MODIFY_CODE
- **Código:** ERR_ACCOUNT_CANNOT_MODIFY_CODE
- **Nombre corto:** Código inmutable
- **Mensaje base:** `error.ledgerAccount.cannotModifyCode` — "El código de la cuenta no puede modificarse una vez registrado"
- **Descripción clínica:** Se lanza cuando se intenta cambiar el código de una cuenta ya creada.
- **Operación / Caso de uso:** UPDATE_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-006
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** `"Intento de modificar código en cuenta ID 321"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("Código inmutable")`
- **Justificación ética:** Protege la unicidad y trazabilidad de las cuentas.
- **Ejemplo de uso:**
  ```java
  if (!Objects.equals(newCode, this.code)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_CANNOT_MODIFY_CODE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (modificar código), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---
