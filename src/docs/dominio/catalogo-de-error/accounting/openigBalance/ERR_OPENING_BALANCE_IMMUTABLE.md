# ERR_OPENING_BALANCE_IMMUTABLE
- **Código:** ERR_OPENING_BALANCE_IMMUTABLE
- **Nombre corto:** Inmutable
- **Mensaje base:** `error.openingBalance.immutable` — "No permite edición una vez registrado"
- **Descripción clínica:** Se lanza cuando se intenta modificar un saldo inicial ya registrado.
- **Operación / Caso de uso:** UPDATE_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-006 — "No permite edición una vez registrado"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** `"Intento de modificar OpeningBalance ID 123 ya registrado"`
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("Inmutable")`
- **Justificación ética:** Protege la trazabilidad y evita alteraciones indebidas.
- **Ejemplo de uso:**
  ```java
  throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_IMMUTABLE);
  ```  
- **Pruebas mínimas requeridas:** Unitario (intento de edición), integración (HTTP 409).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.

---