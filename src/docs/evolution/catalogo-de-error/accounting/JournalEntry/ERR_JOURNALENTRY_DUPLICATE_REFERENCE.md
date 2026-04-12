### ERR_JOURNALENTRY_DUPLICATE_REFERENCE
- **Código:** ERR_JOURNALENTRY_DUPLICATE_REFERENCE
- **Nombre corto:** Referencia duplicada
- **Mensaje base:** `error.journalEntry.duplicateReference` — "La referencia del asiento contable ya existe"
- **Descripción clínica:** Se lanza cuando se intenta registrar un asiento con un número de referencia que ya está en uso. Esto rompe la unicidad necesaria para trazabilidad y auditoría.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-004
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Referencia duplicada: REF-2025-001 ya existe"`
- **Mapa a código existente:** Sustituye validación previa en `InvalidJournalEntryException("Referencia duplicada")`
- **Justificación ética:** Garantiza unicidad y evita confusión en auditorías.
- **Ejemplo de uso:**
  ```java
  if (repository.existsByReference(ref)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_DUPLICATE_REFERENCE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (referencia duplicada), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---