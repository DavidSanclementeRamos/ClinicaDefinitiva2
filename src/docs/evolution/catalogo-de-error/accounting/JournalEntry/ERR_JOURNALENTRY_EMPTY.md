# ERR_JOURNALENTRY_EMPTY
- **Código:** ERR_JOURNALENTRY_EMPTY
- **Nombre corto:** Asiento vacío
- **Mensaje base:** `error.journalEntry.empty` — "El asiento debe tener al menos una línea"
- **Descripción clínica:** Se lanza cuando se intenta contabilizar un asiento sin líneas. Un asiento vacío carece de sentido contable.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-012
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 234 sin líneas registradas"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("El asiento debe tener al menos una línea")`
- **Justificación ética:** Evita registros contables vacíos que no aportan información financiera.
- **Ejemplo de uso:**
  ```java
  if (this.lines.isEmpty()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_EMPTY);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (asiento vacío), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---
