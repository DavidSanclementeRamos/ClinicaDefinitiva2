# ERR_ACCOUNT_INVALID_CODE_LENGTH

- **Código:** ERR_ACCOUNT_INVALID_CODE_LENGTH
- **Nombre corto:** Longitud de código inválida
- **Mensaje base:** `error.ledgerAccount.invalidCodeLength` — "El código de la cuenta debe tener longitud válida (1, 2, 4, 6 u 8 dígitos)"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta crear una cuenta contable con un código cuya longitud no corresponde a los formatos permitidos.  
  La longitud estándar asegura consistencia en la clasificación y evita códigos ambiguos o no auditables.
- **Operación / Caso de uso:** CREAR_LEDGER_ACCOUNT
- **Regla de negocio:** RN-LEDGERACCOUNT-001 — "Código debe tener longitud válida" (ver ADR-XX-ERR_ACCOUNT_INVALID_CODE_LENGTH.md)
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 (Bad Request)
- **Detalle dinámico sugerido:** `"Cuenta con código '12345' inválido: longitud no permitida"`
- **Mapa a código existente:** Sustituye `InvalidLedgerAccountException("El código de la cuenta debe tener longitud válida")`
- **Justificación ética:**  
  Este error protege la integridad del plan contable y evita registros con identificadores no conformes que dificulten la trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (!(code.length() == 1 || code.length() == 2 || code.length() == 4 
        || code.length() == 6 || code.length() == 8)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_INVALID_CODE_LENGTH);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: Crear cuenta con código de longitud inválida → excepción lanzada.
    - Integración: API devuelve HTTP 400 con código de error correspondiente.
- **Changelog / versión:**
    - 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---
