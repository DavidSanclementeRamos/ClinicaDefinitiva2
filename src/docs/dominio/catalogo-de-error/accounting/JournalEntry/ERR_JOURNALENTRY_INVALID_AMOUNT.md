### ERR_JOURNALENTRY_INVALID_AMOUNT
- **Código:** ERR_JOURNALENTRY_INVALID_AMOUNT
- **Nombre corto:** Monto inválido
- **Mensaje base:** `error.journalEntry.invalidAmount` — "El monto debe ser mayor a cero"
- **Descripción clínica:** Se lanza cuando el monto es cero o negativo, lo que invalida la operación contable.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-002
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Monto inválido en asiento ID 456"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Monto inválido")`
- **Justificación ética:** Evita registros contables inconsistentes.
- **Ejemplo de uso:**
  ```java
  if (amount <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_INVALID_AMOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (monto <= 0), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---