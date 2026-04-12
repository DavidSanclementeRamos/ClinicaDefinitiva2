### ERR_JOURNALENTRY_MISSING_ACCOUNT
- **Código:** ERR_JOURNALENTRY_MISSING_ACCOUNT
- **Nombre corto:** Cuenta obligatoria
- **Mensaje base:** `error.journalEntry.missingAccount` — "Debe especificarse una cuenta contable válida"
- **Descripción clínica:** Ocurre cuando se intenta registrar un asiento sin cuenta asociada. Sin cuenta, el asiento no puede ser clasificado ni auditado.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-001
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento con ID 123 sin cuenta contable"`
- **Mapa a código existente:** Sustituye validación previa en `InvalidJournalEntryException("Cuenta obligatoria")`
- **Justificación ética:** Protege la integridad contable y evita registros huérfanos.
- **Ejemplo de uso:**
  ```java
  if (account == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_ACCOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (cuenta nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---