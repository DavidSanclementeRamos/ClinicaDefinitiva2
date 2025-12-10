# ERR_OPENING_BALANCE_MISSING_AMOUNT
- **Código:** ERR_OPENING_BALANCE_MISSING_AMOUNT
- **Nombre corto:** Monto obligatorio
- **Mensaje base:** `error.openingBalance.missingAmount` — "El monto es obligatorio"
- **Descripción clínica:** Se lanza cuando se intenta crear un saldo inicial sin monto definido.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-002 — "El monto es obligatorio"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Intento de crear OpeningBalance sin monto"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Monto obligatorio")`
- **Justificación ética:** Garantiza que todo saldo inicial tenga valor definido para trazabilidad.
- **Ejemplo de uso:**
  ```java
  if (amount == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_MISSING_AMOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (monto nulo), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---