# ERR_OPENING_BALANCE_MISSING_COMPANY
- **Código:** ERR_OPENING_BALANCE_MISSING_COMPANY
- **Nombre corto:** Compañía obligatoria
- **Mensaje base:** `error.openingBalance.missingCompany` — "Debe tener compañía válida"
- **Descripción clínica:** Se lanza cuando se intenta crear un saldo inicial sin compañía asociada.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-005 — "Debe tener compañía válida"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"OpeningBalance sin compañía asociada"`
- **Mapa a código existente:** Sustituye validación en constructor.
- **Justificación ética:** Evita registros sin contexto organizacional.
- **Ejemplo de uso:**
  ```java
  if (companyId == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_MISSING_COMPANY);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (compañía nula), integración (HTTP 400).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---
