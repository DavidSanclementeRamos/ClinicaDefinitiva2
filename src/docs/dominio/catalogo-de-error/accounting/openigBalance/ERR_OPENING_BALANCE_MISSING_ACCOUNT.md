### ERR_OPENING_BALANCE_MISSING_ACCOUNT
- **Código:** ERR_OPENING_BALANCE_MISSING_ACCOUNT
- **Nombre corto:** Cuenta obligatoria
- **Mensaje base:** `error.openingBalance.missingAccount` — "Debe tener cuenta contable válida"
- **Descripción clínica:** Se lanza cuando se intenta crear un saldo inicial sin cuenta asociada.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-004 — "Debe tener cuenta contable válida"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"OpeningBalance sin cuenta contable asociada"`
- **Mapa a código existente:** Sustituye validación en constructor.
- **Justificación ética:** Evita saldos huérfanos sin referencia contable.
- **Ejemplo de uso:**
  ```java
  if (accountId == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_MISSING_ACCOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (cuenta nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---
