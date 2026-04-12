### ERR_JOURNALENTRY_NOT_POSTED_REVERSAL
- **Código:** ERR_JOURNALENTRY_NOT_POSTED_REVERSAL
- **Nombre corto:** Reversa inválida en asiento no contabilizado
- **Mensaje base:** `error.journalEntry.notPostedReversal` — "Solo se pueden reversar asientos contabilizados"
- **Descripción clínica:** Se lanza cuando se intenta reversar un asiento que nunca fue contabilizado. La reversa solo aplica a registros publicados.
- **Operación / Caso de uso:** REVERSAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-016
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Intento de reversa en asiento ID 123 no contabilizado"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Solo se pueden reversar asientos contabilizados")`
- **Justificación ética:** Protege la coherencia contable evitando reversas sobre registros inexistentes.
- **Ejemplo de uso:**
  ```java
  if (!this.posted) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_NOT_POSTED_REVERSAL);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (reversa en asiento no contabilizado), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---