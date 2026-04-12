### ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON
- **Código:** ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON
- **Nombre corto:** Razón obligatoria para reversa
- **Mensaje base:** `error.journalEntry.reversalRequiresReason` — "Se requiere una razón para reversar el asiento"
- **Descripción clínica:** Se lanza cuando se intenta reversar un asiento sin proporcionar motivo. La razón es obligatoria para trazabilidad y auditoría.
- **Operación / Caso de uso:** REVERSAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-017
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Intento de reversa en asiento ID 234 sin razón especificada"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Se requiere una razón para reversar el asiento")`
- **Justificación ética:** Garantiza transparencia y responsabilidad en operaciones contables.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_REVERSAL_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (reversa sin razón), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---