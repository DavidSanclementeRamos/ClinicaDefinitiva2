# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: LedgerAccount (Cuenta Contable)

## Propósito
Representar una cuenta del Plan Único de Cuentas (PUC) colombiano, definiendo la estructura contable jerárquica, naturaleza de cuentas (débito/crédito), y reglas para registro de transacciones. Este agregado protege la coherencia del catálogo contable y permite trazabilidad según normativa colombiana.

---

## CREACIÓN
- Debe tener código numérico único (1-8 dígitos).
- Código solo puede contener dígitos (0-9).
- Código debe tener longitud válida: 1, 2, 4, 6 u 8 dígitos.
- Nombre de cuenta (Name) es obligatorio.
- Naturaleza de cuenta (DEBITO/CREDITO) es obligatoria.
- Se asigna estado inicial ACTIVE por defecto.
- CompanyId asociado es obligatorio.
- Flags requiresThirdParty y requiresDocument son opcionales (false por defecto).

---

## ESTRUCTURA JERÁRQUICA (PUC COLOMBIANO)
- Nivel 1 (1 dígito): Clase (ej: 1=Activo, 2=Pasivo, 3=Patrimonio).
- Nivel 2 (2 dígitos): Grupo (ej: 11=Disponible, 12=Inversiones).
- Nivel 3 (4 dígitos): Cuenta (ej: 1105=Caja, 1110=Bancos).
- Nivel 4 (6 dígitos): Subcuenta (ej: 110505=Caja General).
- Nivel 5 (8 dígitos): Auxiliar (ej: 11050501=Caja Principal).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está ACTIVE.
- No puede modificarse el código una vez registrado (inmutable).
- No puede modificarse la naturaleza de cuenta.
- Puede actualizarse nombre, requiresThirdParty, requiresDocument.
- Cambios sensibles deben registrar auditoría con fecha y responsable.

---

## ACTIVACIÓN / INACTIVACIÓN
- Puede activarse si está inactiva.
- Puede inactivarse solo con motivo obligatorio.
- No puede inactivarse si tiene movimientos contables del mes actual.
- Cuentas inactivas no pueden recibir nuevos movimientos.
- La eliminación física está prohibida; se maneja como estado lógico.

---

## OPERACIONES DE DOMINIO
- getAccountLevel() → Obtiene nivel jerárquico (1-5).
- isAssetAccount() → Verifica si es cuenta de activo (código inicia con 1).
- isLiabilityAccount() → Verifica si es cuenta de pasivo (código inicia con 2).
- isEquityAccount() → Verifica si es cuenta de patrimonio (código inicia con 3).
- isIncomeAccount() → Verifica si es cuenta de ingreso (código inicia con 4).
- isExpenseAccount() → Verifica si es cuenta de gasto (código inicia con 5).
- isCostAccount() → Verifica si es cuenta de costos (código inicia con 6).
- isCostOfSalesAccount() → Verifica si es cuenta de costo de ventas (código inicia con 7).
- getParentCode() → Obtiene código padre en jerarquía.
- validateMovementRequirements() → Valida si movimiento cumple requisitos (tercero, documento).
- getFullDescription() → Retorna: "código - nombre (naturaleza)".

---

## INVARIANTES GLOBALES
- Una cuenta activa debe tener código, nombre y naturaleza válidos.
- No puede haber dos cuentas con el mismo código en la misma compañía.
- El código debe seguir estructura jerárquica PUC (1, 2, 4, 6, u 8 dígitos).
- Una cuenta que requiere tercero no puede registrar movimientos sin tercero.
- Una cuenta que requiere documento no puede registrar movimientos sin documento.
- Cuentas inactivas no pueden recibir movimientos nuevos.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado, activación, inactivación.
- Se registra motivo obligatorio al inactivar.
- Sistema emite alertas al intentar usar cuenta inactiva.
- Auditoría completa de modificaciones con fecha y responsable.
- Integración con validaciones de asientos contables.

---

## Justificación Semántica
Estas reglas aseguran que el catálogo contable sea coherente con el PUC colombiano, protegen la integridad jerárquica, evitan movimientos inválidos y permiten auditar cada decisión. El modelo está listo para integrarse con asientos contables, reportes financieros, cumplimiento DIAN y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-LEDGERACCOUNT-001**
- Descripción: Código debe tener longitud válida (1, 2, 4, 6, u 8 dígitos).
- Condición: code.length() != {1, 2, 4, 6, 8} al invocar creación.
- Consecuencia: Se rechaza operación con InvalidLedgerAccountException.
- Error asociado: ERR_ACCOUNT_INVALID_CODE_LENGTH

**RN-LEDGERACCOUNT-002**
- Descripción: Código solo puede contener dígitos numéricos.
- Condición: code no cumple patrón [0-9]{1,8}.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_ACCOUNT_INVALID_CODE_FORMAT

**RN-LEDGERACCOUNT-003**
- Descripción: Naturaleza de cuenta es obligatoria.
- Condición: LedgerAccount.nature == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_ACCOUNT_MISSING_NATURE

**RN-LEDGERACCOUNT-004**
- Descripción: Solo puede editarse si está activa.
- Condición: LedgerAccount.active == false al invocar updateAccountInformation().
- Consecuencia: Se rechaza operación con InvalidLedgerAccountException.
- Error asociado: ERR_ACCOUNT_NOT_EDITABLE

**RN-LEDGERACCOUNT-005**
- Descripción: Inactivación requiere motivo obligatorio.
- Condición: inactivate(reason) con reason == null || reason.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_ACCOUNT_INACTIVATION_REQUIRES_REASON

**RN-LEDGERACCOUNT-006**
- Descripción: No puede modificarse el código una vez registrado.
- Condición: Intento de modificar code después de creación.
- Consecuencia: Campo inmutable, operación rechazada.
- Error asociado: ERR_ACCOUNT_CANNOT_MODIFY_CODE

**RN-LEDGERACCOUNT-007**
- Descripción: Movimiento debe cumplir requisitos de tercero si la cuenta lo requiere.
- Condición: requiresThirdParty == true && hasThirdParty == false al registrar movimiento.
- Consecuencia: Se rechaza operación con InvalidLedgerAccountException.
- Error asociado: ERR_ACCOUNT_REQUIRES_THIRD_PARTY

**RN-LEDGERACCOUNT-008**
- Descripción: Movimiento debe cumplir requisitos de documento si la cuenta lo requiere.
- Condición: requiresDocument == true && hasDocument == false al registrar movimiento.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_ACCOUNT_REQUIRES_DOCUMENT

**RN-LEDGERACCOUNT-009**
- Descripción: Código debe ser único por compañía.
- Condición: Existe otra LedgerAccount con mismo code y companyId.
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_ACCOUNT_DUPLICATE_CODE

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - catálogo base.
- ADR-17 (Arquitectura): Manejo de plan de cuenta y asiento contable.
- ADR-28 (Dominio): Conocimientos contables - PUC colombiano.
- ADR-32 (Dominio): Reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas - validación de movimientos.

---

## Eventos de Dominio
- LedgerAccountRegistered: Al crear una nueva cuenta.
- LedgerAccountUpdated: Al actualizar información de cuenta.
- LedgerAccountActivated: Al activar cuenta inactiva.
- LedgerAccountInactivated: Al inactivar cuenta.
- LedgerAccountMovementValidated: Al validar requisitos de movimiento.

---

## Clasificación de Cuentas PUC Colombia
**Clase 1 - ACTIVO** (Naturaleza: DEBITO)
- 1105: Caja
- 1110: Bancos
- 1305: Clientes
- 1435: Inventarios

**Clase 2 - PASIVO** (Naturaleza: CREDITO)
- 2205: Proveedores
- 2335: Costos y gastos por pagar
- 2365: Retenciones y aportes

**Clase 3 - PATRIMONIO** (Naturaleza: CREDITO)
- 3105: Capital social
- 3605: Utilidad del ejercicio

**Clase 4 - INGRESOS** (Naturaleza: CREDITO)
- 4135: Servicios de salud
- 4175: Devoluciones en ventas (naturaleza DEBITO)

**Clase 5 - GASTOS** (Naturaleza: DEBITO)
- 5105: Gastos de personal
- 5195: Diversos

**Clase 6 - COSTOS DE VENTAS** (Naturaleza: DEBITO)
- 6135: Servicios de salud