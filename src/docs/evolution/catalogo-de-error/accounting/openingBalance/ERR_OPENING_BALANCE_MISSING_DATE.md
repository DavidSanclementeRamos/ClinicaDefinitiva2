# ERR_OPENING_BALANCE_MISSING_DATE
- **Código:** ERR_OPENING_BALANCE_MISSING_DATE
- **Nombre corto:** Fecha obligatoria
- **Mensaje base:** `error.openingBalance.missingDate` — "La fecha es obligatoria"
- **Descripción clínica:** Se lanza cuando se intenta crear un saldo inicial sin fecha definida.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-003 — "La fecha es obligatoria"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"OpeningBalance sin fecha definida"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Fecha obligatoria")`
- **Justificación ética:** Evita registros sin temporalidad, protegiendo la coherencia contable.
- **Ejemplo de uso:**
  ```java
  if (date == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_MISSING_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (fecha nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---