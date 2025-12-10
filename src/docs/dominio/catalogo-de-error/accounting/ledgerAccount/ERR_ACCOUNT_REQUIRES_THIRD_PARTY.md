# ERR_ACCOUNT_REQUIRES_THIRD_PARTY
- **Código:** ERR_ACCOUNT_REQUIRES_THIRD_PARTY
- **Nombre corto:** Requiere tercero
- **Mensaje base:** `error.ledgerAccount.requiresThirdParty` — "La cuenta requiere un tercero para registrar el movimiento"
- **Descripción clínica:** Se lanza cuando la cuenta exige tercero pero el movimiento no lo incluye.
- **Operación / Caso de uso:** REGISTRAR_MOVIMIENTO
- **Regla de negocio



No, todavía falta completar la documentación de las demás entradas del catálogo de errores de **LedgerAccount** (RN‑LEDGERACCOUNT‑007 a RN‑LEDGERACCOUNT‑011). Te muestro el bloque final para que tengas **todo el catálogo completo**:

---

### ERR_ACCOUNT_REQUIRES_THIRD_PARTY
- **Código:** ERR_ACCOUNT_REQUIRES_THIRD_PARTY
- **Nombre corto:** Requiere tercero
- **Mensaje base:** `error.ledgerAccount.requiresThirdParty` — "La cuenta requiere un tercero para registrar el movimiento"
- **Descripción clínica:** Se lanza cuando la cuenta está marcada como `requiresThirdParty` pero el movimiento no incluye un tercero asociado.
- **Operación / Caso de uso:** REGISTRAR_MOVIMIENTO
- **Regla de negocio:** RN-LEDGERACCOUNT-007
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Cuenta 1105 - Caja General requiere tercero y no fue proporcionado"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("La cuenta requiere un tercero")`
- **Justificación ética:** Garantiza que las cuentas que requieren terceros no se usen sin la información necesaria.
- **Ejemplo de uso:**
  ```java
  if (this.requiresThirdParty && !hasThirdParty) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_REQUIRES_THIRD_PARTY);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (movimiento sin tercero), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---