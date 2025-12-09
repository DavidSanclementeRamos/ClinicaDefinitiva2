# ERR_JOURNALENTRY_LINE_NOT_FOUND
- **Código:** ERR_JOURNALENTRY_LINE_NOT_FOUND
- **Nombre corto:** Línea inexistente
- **Mensaje base:** `error.journalEntry.lineNotFound` — "La línea no existe en el asiento"
- **Descripción clínica:** Se lanza cuando se intenta eliminar una línea que no pertenece al asiento contable. Esto evita inconsistencias en la estructura del asiento.
- **Operación / Caso de uso:** ELIMINAR_LINEA_ASIENTO
- **Regla de negocio:** RN-JOURNALENTRY-011
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 404 (Not Found)
- **Detalle dinámico sugerido:** `"Intento de eliminación de línea ID 999 inexistente en asiento ID 123"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("La línea no existe en el asiento")`
- **Justificación ética:** Protege la integridad del asiento evitando operaciones sobre elementos inexistentes.
- **Ejemplo de uso:**
  ```java
  if (!this.lines.remove(line)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_LINE_NOT_FOUND);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (línea inexistente), integración (HTTP 404).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---
