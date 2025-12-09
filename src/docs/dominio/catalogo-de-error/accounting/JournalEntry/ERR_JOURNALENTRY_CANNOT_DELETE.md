### ERR_JOURNALENTRY_CANNOT_DELETE
- **Código:** ERR_JOURNALENTRY_CANNOT_DELETE
- **Nombre corto:** Asiento no eliminable
- **Mensaje base:** `error.journalEntry.cannotDelete` — "El asiento no puede eliminarse si está conciliado"
- **Descripción clínica:** Se lanza cuando se intenta borrar un asiento que ya fue conciliado. La eliminación afectaría la consistencia de los estados financieros.
- **Operación / Caso de uso:** ELIMINAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-009
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409 (Conflict)
- **Detalle dinámico sugerido:** `"Intento de eliminación en asiento ID 789 conciliado"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("No se puede eliminar")`
- **Justificación ética:** Protege la consistencia de registros conciliados.
- **Ejemplo de uso:**
  ```java
  if (reconciled) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_CANNOT_DELETE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (eliminar asiento conciliado), integración (HTTP 409).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---