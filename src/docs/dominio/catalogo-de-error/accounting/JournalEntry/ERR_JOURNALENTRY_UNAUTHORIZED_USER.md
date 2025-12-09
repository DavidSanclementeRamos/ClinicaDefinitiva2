### ERR_JOURNALENTRY_UNAUTHORIZED_USER
- **Código:** ERR_JOURNALENTRY_UNAUTHORIZED_USER
- **Nombre corto:** Usuario no autorizado
- **Mensaje base:** `error.journalEntry.unauthorizedUser` — "El usuario no tiene permisos para registrar asientos contables"
- **Descripción clínica:** Se lanza cuando un usuario sin permisos intenta crear o modificar un asiento contable.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE / EDITAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-010
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 403 (Forbidden)
- **Detalle dinámico sugerido:** `"Usuario ID 321 sin permisos para asiento ID 654"`
- **Mapa a código existente:** Sustituye validación previa en `InvalidJournalEntryException("Usuario no autorizado")`
- **Justificación ética:** Garantiza que solo usuarios autorizados puedan manipular registros contables.
- **Ejemplo de uso:**
  ```java
  if (!user.hasPermission("REGISTER_JOURNAL_ENTRY")) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_UNAUTHORIZED_USER);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (usuario sin permisos), integración (HTTP 403).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---