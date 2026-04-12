# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: OpeningBalance (Saldo Inicial)

## Propósito
Representar el saldo inicial de una cuenta contable al inicio de un período contable (usualmente al inicio del año fiscal). Este agregado protege la integridad de los balances de apertura y permite trazabilidad del estado financiero inicial para cierres contables y reportes.

---

## CREACIÓN
- Debe tener compañía (CompanyId) válida asociada.
- Debe tener cuenta contable (LedgerAccountId) válida.
- Monto (Money) es obligatorio y debe ser mayor a cero.
- Fecha es obligatoria y se registra por defecto como fecha actual.
- Tercero (ThirdPartiesId) es opcional (solo si la cuenta lo requiere).
- No puede crearse con monto negativo o cero.
- Se registra automáticamente fecha de creación.

---

## RESTRICCIONES DE EDICIÓN
- **Los saldos iniciales son INMUTABLES una vez registrados**.
- No permite operaciones de actualización o edición.
- No permite eliminación física.
- Cualquier corrección debe hacerse mediante asiento contable de ajuste.

---

## VALIDACIONES DE CREACIÓN
- Debe validar que la cuenta contable exista y esté activa.
- Si la cuenta requiere tercero, el saldo inicial debe incluir tercero.
- No puede registrarse saldo inicial duplicado para misma cuenta/tercero/período.
- El monto debe respetar la naturaleza de la cuenta (débito/crédito).
- La fecha debe corresponder al inicio del período contable.

---

## OPERACIONES DE DOMINIO
- registerOpeningBalance() → Factory method para crear saldo inicial.
- getMonto() → Retorna monto del saldo (Money).
- getCuentaId() → Retorna ID de cuenta contable asociada.
- getThirdPartiesId() → Retorna ID de tercero (si aplica).
- getFecha() → Retorna fecha del saldo inicial.

---

## INVARIANTES GLOBALES
- Un saldo inicial debe tener compañía, cuenta y monto válidos.
- El monto siempre debe ser mayor a cero.
- No puede haber dos saldos iniciales para misma cuenta/tercero/período.
- Los saldos iniciales son inmutables (no editables ni eliminables).
- Si la cuenta requiere tercero, el saldo debe incluir tercero.
- La fecha debe corresponder al inicio del período contable.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra fecha exacta de creación del saldo inicial.
- Se vincula con compañía y cuenta contable.
- Sistema emite evento al registrar saldo inicial.
- No se registran modificaciones (inmutable por diseño).
- Integración con balance de apertura y reportes de cierre.

---

## Justificación Semántica
Los saldos iniciales son críticos para la continuidad contable entre períodos. Su inmutabilidad garantiza la integridad histórica y permite auditar el estado financiero de apertura. Cualquier error debe corregirse mediante asientos de ajuste, no modificando el saldo original. El modelo cumple con normativa contable colombiana (PUC, NIIF) y está listo para exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-OPENINGBALANCE-001**
- Descripción: El monto debe ser mayor a cero.
- Condición: OpeningBalance.amount.isNegativeOrZero() == true al invocar creación.
- Consecuencia: Se rechaza operación con InvalidJournalEntryException.
- Error asociado: ERR_OPENING_BALANCE_INVALID_AMOUNT

**RN-OPENINGBALANCE-002**
- Descripción: El monto es obligatorio.
- Condición: OpeningBalance.amount == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_OPENING_BALANCE_MISSING_AMOUNT

**RN-OPENINGBALANCE-003**
- Descripción: La fecha es obligatoria.
- Condición: OpeningBalance.date == null al invocar creación.
- Consecuencia: Se rechaza operación con InvalidJournalEntryException.
- Error asociado: ERR_OPENING_BALANCE_MISSING_DATE

**RN-OPENINGBALANCE-004**
- Descripción: Debe tener cuenta contable válida.
- Condición: OpeningBalance.cuentaId == null al invocar creación.
- Consecuencia: Se rechaza operación (validación en constructor).
- Error asociado: ERR_OPENING_BALANCE_MISSING_ACCOUNT

**RN-OPENINGBALANCE-005**
- Descripción: Debe tener compañía válida.
- Condición: OpeningBalance.companyId == null al invocar creación.
- Consecuencia: Se rechaza operación (validación en constructor).
- Error asociado: ERR_OPENING_BALANCE_MISSING_COMPANY

**RN-OPENINGBALANCE-006**
- Descripción: No permite edición una vez registrado (inmutable).
- Condición: Intento de modificar cualquier campo después de creación.
- Consecuencia: Operación no disponible, clase final sin setters.
- Error asociado: ERR_OPENING_BALANCE_IMMUTABLE

**RN-OPENINGBALANCE-007**
- Descripción: Si la cuenta requiere tercero, debe incluir tercero.
- Condición: LedgerAccount.requiresThirdParty == true && OpeningBalance.thirdPartiesId == null.
- Consecuencia: Se rechaza operación (validación cruzada).
- Error asociado: ERR_OPENING_BALANCE_REQUIRES_THIRD_PARTY

**RN-OPENINGBALANCE-008**
- Descripción: No puede registrarse saldo duplicado para misma cuenta/tercero/período.
- Condición: Existe OpeningBalance con mismo companyId, cuentaId, thirdPartiesId, período.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_OPENING_BALANCE_DUPLICATE

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - saldos de apertura.
- ADR-17 (Arquitectura): Manejo de plan de cuenta y asiento contable.
- ADR-28 (Dominio): Conocimientos contables - cierre y apertura de períodos.
- ADR-32 (Dominio): Reglas de negocio por agregado.

---

## Eventos de Dominio
- OpeningBalanceRegistered: Al crear un nuevo saldo inicial.
- OpeningBalanceValidated: Al validar coherencia con cuenta contable.

---

## Proceso de Registro de Saldos Iniciales

**1. Preparación (Cierre Período Anterior)**
- Generar Balance de Comprobación del período anterior.
- Verificar que débitos = créditos.
- Generar Estado de Resultados (Pérdidas y Ganancias).

**2. Registro de Saldos Iniciales (Nuevo Período)**
- **Cuentas de Balance (1, 2, 3)**: Se trasladan con su saldo.
    - Activos (1): Si tenían saldo débito → OpeningBalance con monto positivo.
    - Pasivos (2): Si tenían saldo crédito → OpeningBalance con monto positivo.
    - Patrimonio (3): Se ajusta con resultado del ejercicio anterior.

- **Cuentas de Resultados (4, 5, 6, 7)**: NO se trasladan (inician en cero).
    - Ingresos (4): Inician en 0 cada período.
    - Gastos (5): Inician en 0 cada período.
    - Costos (6, 7): Inician en 0 cada período.

**3. Validación Post-Registro**
- Sumar todos los OpeningBalance débito.
- Sumar todos los OpeningBalance crédito.
- Verificar: sum(débitos) = sum(créditos).

**4. Ejemplo Práctico**

```
Cierre 2024:
- Caja (1105): Saldo débito $5,000,000
- Bancos (1110): Saldo débito $20,000,000
- Proveedores (2205): Saldo crédito $3,000,000
- Capital (3105): Saldo crédito $15,000,000
- Utilidad del Ejercicio (3605): Saldo crédito $7,000,000

Apertura 2025:
OpeningBalance(cuentaId=1105, amount=5,000,000, nature=DEBITO)
OpeningBalance(cuentaId=1110, amount=20,000,000, nature=DEBITO)
OpeningBalance(cuentaId=2205, amount=3,000,000, nature=CREDITO)
OpeningBalance(cuentaId=3105, amount=15,000,000, nature=CREDITO)
OpeningBalance(cuentaId=3605, amount=7,000,000, nature=CREDITO)

Validación:
Débitos: 5M + 20M = 25M
Créditos: 3M + 15M + 7M = 25M
✓ Balanceado
```

---

## Corrección de Errores

Si se detecta un error en un saldo inicial:

1. **NO modificar el OpeningBalance** (inmutable).
2. Crear un **asiento de ajuste**:
   ```
   JournalEntry(
     description: "Ajuste de saldo inicial - Caja",
     lines: [
       debit(cuenta=1105, amount=diferencia, description="Ajuste saldo inicial"),
       credit(cuenta=3605, amount=diferencia, description="Ajuste utilidad retenida")
     ]
   )
   ```

3. El **nuevo saldo efectivo** será: `OpeningBalance + AsientoDeAjuste`.

---

## Integración con Reportes

- **Balance General**: Usa saldos iniciales + movimientos del período.
- **Balance de Comprobación**: Inicia con saldos iniciales.
- **Estado de Resultados**: NO usa saldos iniciales (inicia en cero).
- **Flujo de Caja**: Usa saldo inicial de cuentas de efectivo (1105, 1110).