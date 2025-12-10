# ERR_OPENING_BALANCE_INVALID_AMOUNT
- **Código:** ERR_OPENING_BALANCE_INVALID_AMOUNT
- **Nombre corto:** Monto inválido
- **Mensaje base:** `error.openingBalance.invalidAmount` — "El monto debe ser mayor a cero"
- **Descripción clínica:** Se lanza cuando se intenta registrar un saldo inicial con monto igual a cero o negativo.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-001 — "El monto debe ser mayor a cero"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"OpeningBalance con monto -100 inválido"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Monto inválido")`
- **Justificación ética:** Evita registros contables inconsistentes y protege la integridad financiera.
- **Ejemplo de uso:**
  ```java
  if (amount == null || amount.signum() <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_INVALID_AMOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (monto cero/negativo), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---