### ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH
- **Código:** ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH
- **Nombre corto:** Descripción demasiado corta
- **Mensaje base:** `error.journalEntry.invalidDescriptionLength` — "La descripción debe tener al menos 5 caracteres"
- **Descripción clínica:** Se lanza cuando la descripción del asiento es demasiado breve, impidiendo claridad y trazabilidad.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-020
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 123 con descripción 'AB' inválida"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("La descripción debe tener al menos 5 caracteres")`
- **Justificación ética:** Garantiza que los registros contables tengan descripciones útiles y auditables.
- **Ejemplo de uso:**
  ```java
  if (description.trim().length() < 5) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_INVALID_DESCRIPTION_LENGTH);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (descripción < 5), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---