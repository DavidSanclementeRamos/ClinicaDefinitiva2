# ERR_JOURNALENTRY_FUTURE_DATE
- **Código:** ERR_JOURNALENTRY_FUTURE_DATE
- **Nombre corto:** Fecha futura al contabilizar
- **Mensaje base:** `error.journalEntry.futureDate` — "No se puede contabilizar un asiento con fecha futura"
- **Descripción clínica:** Se lanza cuando se intenta contabilizar un asiento cuya fecha es posterior a la fecha actual.
- **Operación / Caso de uso:** POST_ASIENTO
- **Regla de negocio:** RN-JOURNALENTRY-015
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Intento de contabilizar asiento ID 567 con fecha futura 2026-01-01"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("No se puede contabilizar fecha futura")`
- **Justificación ética:** Evita registros contables anticipados que distorsionan la realidad financiera.
- **Ejemplo de uso:**
  ```java
  if (this.date.isAfter(LocalDate.now())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_FUTURE_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (fecha futura), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---
