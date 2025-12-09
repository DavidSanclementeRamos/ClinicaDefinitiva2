### ERR_JOURNALENTRY_NOT_EDITABLE
- **Código:** ERR_JOURNALENTRY_NOT_EDITABLE
- **Nombre corto:** Asiento no editable
- **Mensaje base:** `error.journalEntry.notEditable` — "El asiento no puede editarse una vez publicado"
- **Descripción clínica:** Se lanza cuando se intenta modificar un asiento ya contabilizado. Una vez publicado, debe permanecer inmutable para garantizar la integridad histórica.
- **Operación / Caso de uso:** EDITAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-008
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Intento de edición en asiento ID 678 ya contabilizado"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("No editable")`
- **Justificación ética:** Protege la inmutabilidad de registros contables publicados.
- **Ejemplo de uso:**
  ```java
  if (posted) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_NOT_EDITABLE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (editar asiento publicado), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---