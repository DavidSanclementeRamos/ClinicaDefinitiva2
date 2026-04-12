# ERR_JOURNALENTRY_MISSING_DATE
- **Código:** ERR_JOURNALENTRY_MISSING_DATE
- **Nombre corto:** Fecha obligatoria
- **Mensaje base:** `error.journalEntry.missingDate` — "La fecha es obligatoria"
- **Descripción clínica:** Se lanza cuando se intenta crear un asiento sin fecha. La fecha es esencial para ubicar el registro en el período contable correcto.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-022
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Asiento ID 234 sin fecha definida"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("La fecha es obligatoria")`
- **Justificación ética:** Evita registros contables sin referencia temporal.
- **Ejemplo de uso:**
  ```java
  if (date == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (fecha nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry. 

---