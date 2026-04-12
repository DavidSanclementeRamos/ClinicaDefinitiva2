# ERR_JOURNALENTRY_INSUFFICIENT_LINES
- **Código:** ERR_JOURNALENTRY_INSUFFICIENT_LINES
- **Nombre corto:** Partida doble insuficiente
- **Mensaje base:** `error.journalEntry.insufficientLines` — "El asiento debe tener al menos dos líneas (partida doble)"
- **Descripción clínica:** Se lanza cuando el asiento tiene menos de dos líneas, lo que rompe la regla de partida doble (débito y crédito).
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-013
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 345 con solo una línea registrada"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Debe tener al menos dos líneas")`
- **Justificación ética:** Garantiza la regla fundamental de la contabilidad: partida doble.
- **Ejemplo de uso:**
  ```java
  if (this.lines.size() < 2) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_INSUFFICIENT_LINES);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (una sola línea), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---