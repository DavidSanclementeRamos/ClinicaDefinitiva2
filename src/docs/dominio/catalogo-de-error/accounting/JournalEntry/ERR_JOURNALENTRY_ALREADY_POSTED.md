# ERR_JOURNALENTRY_ALREADY_POSTED
- **Código:** ERR_JOURNALENTRY_ALREADY_POSTED
- **Nombre corto:** Asiento ya contabilizado
- **Mensaje base:** `error.journalEntry.alreadyPosted` — "El asiento ya está contabilizado"
- **Descripción clínica:** Se lanza cuando se intenta contabilizar un asiento que ya fue publicado. La operación es redundante e inválida.
- **Operación / Caso de uso:** POST_ASIENTO
- **Regla de negocio:** RN-JOURNALENTRY-014
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Intento de contabilizar asiento ID 456 ya publicado"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("El asiento ya está contabilizado")`
- **Justificación ética:** Evita duplicidad de registros contables.
- **Ejemplo de uso:**
  ```java
  if (this.posted) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_ALREADY_POSTED);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (asiento ya contabilizado), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---