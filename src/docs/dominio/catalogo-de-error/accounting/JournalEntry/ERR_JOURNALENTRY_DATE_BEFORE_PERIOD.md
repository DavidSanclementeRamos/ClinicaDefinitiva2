### ERR_JOURNALENTRY_DATE_BEFORE_PERIOD
- **Código:** ERR_JOURNALENTRY_DATE_BEFORE_PERIOD
- **Nombre corto:** Fecha fuera de período
- **Mensaje base:** `error.journalEntry.dateBeforePeriod` — "La fecha del asiento no puede ser anterior al inicio del período contable"
- **Descripción clínica:** Se lanza cuando la fecha del asiento es anterior al período contable vigente, lo que invalida la operación.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-006
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 345 con fecha 2024-12-31 fuera de período 2025"`
- **Mapa a código existente:** Sustituye validación previa en `InvalidJournalEntryException("Fecha fuera de período")`
- **Justificación ética:** Protege la consistencia temporal de los registros contables.
- **Ejemplo de uso:**
  ```java
  if (date.isBefore(period.getStartDate())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_DATE_BEFORE_PERIOD);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (fecha fuera de período), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---