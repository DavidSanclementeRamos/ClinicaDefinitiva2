### ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD
- **Código:** ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD
- **Nombre corto:** Descripción obligatoria
- **Mensaje base:** `error.journalEntry.missingDescriptionField` — "La descripción es obligatoria"
- **Descripción clínica:** Se lanza cuando se intenta crear un asiento contable sin descripción. La ausencia de detalle impide la trazabilidad y comprensión del registro.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-007
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 567 sin descripción"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Descripción obligatoria")`
- **Justificación ética:** Garantiza claridad y trazabilidad en registros contables.
- **Ejemplo de uso:**
  ```java
  if (description == null || description.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_DESCRIPTION_FIELD);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (descripción nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---