### ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER
- **Código:** ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER
- **Nombre corto:** Número de documento inválido
- **Mensaje base:** `error.journalEntry.invalidDocumentNumber` — "El número de documento debe tener al menos 1 carácter"
- **Descripción clínica:** Se lanza cuando el número de documento es demasiado corto o inválido.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-019
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 456 con número de documento inválido"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("El número de documento debe tener al menos 1 carácter")`
- **Justificación ética:** Garantiza que los registros tengan identificadores válidos y auditables.
- **Ejemplo de uso:**
  ```java
  if (documentNumber.trim().length() < 1) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_INVALID_DOCUMENT_NUMBER);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (documento vacío), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---