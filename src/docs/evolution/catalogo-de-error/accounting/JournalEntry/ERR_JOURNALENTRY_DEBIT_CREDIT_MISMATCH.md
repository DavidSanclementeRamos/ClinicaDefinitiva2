
### ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH
- **Código:** ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH
- **Nombre corto:** Desbalance débitos/créditos
- **Mensaje base:** `error.journalEntry.debitCreditMismatch` — "Los débitos y créditos deben estar balanceados"
- **Descripción clínica:** Se lanza cuando la suma de débitos no coincide con la de créditos.
- **Operación / Caso de uso:** VALIDAR_BALANCE_ASIENTO
- **Regla de negocio:** RN-JOURNALENTRY-003
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Asiento ID 789 desbalanceado: débitos=100, créditos=90"`
- **Mapa a código existente:** Sustituye validación en `validateBalance()`
- **Justificación ética:** Garantiza la partida doble y la integridad contable.
- **Ejemplo de uso:**
  ```java
  if (!balanced) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_DEBIT_CREDIT_MISMATCH);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (desbalance), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---