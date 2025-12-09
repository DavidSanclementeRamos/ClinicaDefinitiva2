# ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER
- **Código:** ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER
- **Nombre corto:** Número de documento obligatorio
- **Mensaje base:** `error.journalEntry.missingDocumentNumber` — "El número de documento es obligatorio"
- **Descripción clínica:** Se lanza cuando se intenta crear un asiento sin número de documento. Este campo es esencial para identificación y trazabilidad.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-018
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 345 sin número de documento"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("El número de documento es obligatorio")`
- **Justificación ética:** Evita registros contables sin identificación formal.
- **Ejemplo de uso:**
  ```java
  if (documentNumber == null || documentNumber.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_DOCUMENT_NUMBER);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (documento nulo), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---