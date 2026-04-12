# ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY**

## Descripción
Esta validación asegura que, cuando una cuenta requiere un tercero, dicho tercero esté presente en el balance de apertura. Si no se proporciona, se genera un error.

**Operación**: Validaciones iniciales de apertura.  
**Regla**: RN-OPENINGBALANCE-007.   
**Contexto**: OPENINGBALANCE.  
**Tipo**: Validación clínica.  
**Severidad**: ERROR.  
**HTTP**: 400.

**Detalle dinámico**: "Cuenta 1105 requiere tercero y no fue proporcionado en OpeningBalance".

**Changelog**: 2025-12-09, Autor: David — Alta inicial catálogo OpeningBalance.

**Pseudocódigo**:
```  
if (ledgerAccount.requiresThirdParty && thirdPartiesId == null)  
  throw DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY);
```
### ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY
- **Código:** ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY
- **Nombre corto:** Requiere tercero
- **Mensaje base:** `error.openingBalance.requiresThirdParty` — "Debe incluir tercero si la cuenta lo requiere"
- **Descripción clínica:** Se lanza cuando la cuenta asociada al saldo inicial está configurada para requerir tercero, pero el registro no provee un tercero. Esto previene saldos iniciales sin vínculo relacional cuando la política contable exige identificación de terceros.
- **Operación / Caso de uso:** CREAR_OPENING_BALANCE
- **Regla de negocio:** RN-OPENINGBALANCE-007 — "Si la cuenta requiere tercero, debe incluir tercero"
- **Contexto del agregado:** OPENINGBALANCE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Cuenta 1105 requiere tercero y no fue proporcionado en OpeningBalance"
- **Mapa a código existente:** Sustituye `InvalidJournalEntryException("La cuenta requiere un tercero")`
- **Justificación ética:** Garantiza la trazabilidad y responsabilidad en registros que exigen identificación de terceros, evitando información incompleta en el plan contable.
- **Ejemplo de uso:**
  ```java
  if (ledgerAccount.requiresThirdParty() && thirdPartiesId == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY);
  }
  ```
- **Pruebas mínimas requeridas:** Unitario (cuenta requiere tercero y tercero nulo), integración (HTTP 400 con código de error).
- **Changelog / versión:** 2025-12-09 — Autor: David — Alta inicial catálogo OpeningBalance.
