### ERR_JOURNALENTRY_DATE_IN_FUTURE
- **Código:** ERR_JOURNALENTRY_DATE_IN_FUTURE
- **Nombre corto:** Fecha futura inválida
- **Mensaje base:** `error.journalEntry.dateInFuture` — "La fecha del asiento no puede estar en el futuro"
- **Descripción clínica:** Se lanza cuando la fecha del asiento es posterior a la fecha actual, lo que invalida la contabilización.
- **Operación / Caso de uso:** POST_ASIENTO
- **Regla de negocio:** RN-JOURNALENTRY-005
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 234 con fecha futura 2026-01-01"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Fecha futura")`
- **Justificación ética:** Evita registros contables anticipados que distorsionan la realidad financiera.
- **Ejemplo de uso:**
  ```java
  if (date.isAfter(LocalDate.now())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_DATE_IN_FUTURE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (fecha futura), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---