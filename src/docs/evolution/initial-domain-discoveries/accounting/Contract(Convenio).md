# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Contract (Convenio)

## Propósito
Representar convenios o contratos con terceros (EPS, aseguradoras, empresas), gestionando cobertura, vigencia, condiciones contractuales y estados operativos. Este agregado protege la integridad de las relaciones comerciales y permite trazabilidad completa de acuerdos clínicos y administrativos.

---

## CREACIÓN
- Debe tener un tercero (ThirdParties) y una compañía (Company) válidos.
- Debe especificarse tipo de cobertura y nombre del convenio.
- La fecha de inicio por defecto es la fecha actual del sistema.
- La fecha de fin debe ser posterior a la fecha de inicio.
- No puede crearse con fecha de fin en el pasado.
- Se asigna estado inicial ACTIVE.
- La tasa de cobertura (coverageRate) es opcional.
- Debe registrar origen del convenio (web, presencial, referido).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está en estado ACTIVE.
- No puede editarse si está vencido.
- No puede modificarse la fecha de inicio una vez registrado.
- Cambios en información general (nombre, descripción, origen) requieren estado activo.
- La extensión de vigencia solo permite fechas posteriores a la fecha de fin actual.
- Cambios sensibles deben registrar auditoría con fecha y responsable.

---

## SUSPENSIÓN / TERMINACIÓN
- Solo puede suspenderse si está en estado ACTIVE.
- Debe registrar motivo obligatorio para suspender.
- Solo puede reactivarse si está en estado SUSPENDED.
- No puede reactivarse si está vencido.
- La terminación requiere motivo obligatorio.
- Un contrato terminado no puede volver a estado ACTIVE.

---

## OPERACIONES DE DOMINIO
- isExpiredAt(fechaHora) → Verifica si el contrato está vencido en fecha específica.
- isActiveAndValid() → Verifica que esté ACTIVE y no vencido.
- isNearExpiration() → Detecta si está dentro de los 30 días previos al vencimiento.
- getDaysRemaining() → Calcula días restantes de vigencia.
- extendContract(nuevaFechaFin) → Extiende vigencia con validaciones.
- suspend(motivo) → Suspende temporalmente el contrato.
- reactivate() → Reactiva contrato suspendido si no está vencido.
- terminate(motivo) → Finaliza contrato antes de vencimiento.

---

## INVARIANTES GLOBALES
- Un contrato activo no puede tener fecha de fin en el pasado.
- La fecha de fin siempre debe ser posterior a la fecha de inicio.
- No puede haber dos contratos activos para el mismo tercero con fechas solapadas.
- Un contrato suspendido o terminado no puede procesarse en facturación.
- El tipo de cobertura no puede ser nulo ni vacío.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado, extensión, suspensión y terminación.
- Se puede emitir un Outcome al intentar usar un contrato vencido.
- Se registra el tercero asociado y la compañía propietaria.
- Sistema de alertas automáticas 30 días antes del vencimiento.
- Auditoría completa de modificaciones con fecha y responsable.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de convenio sea coherente, trazable y legalmente válido. Protegen la continuidad comercial, evitan operaciones con contratos inválidos y permiten auditar cada decisión relevante en el ciclo de vida del convenio. El modelo está listo para integrarse con facturación, reportes financieros y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-CONTRACT-001**
- Descripción: La fecha de fin debe ser posterior a la fecha de inicio.
- Condición: Contract.endDate < Contract.startDate al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_CONTRACT_INVALID_DATES

**RN-CONTRACT-002**
- Descripción: Solo puede editarse si está en estado ACTIVE y no vencido.
- Condición: Contract.status != ACTIVE || Contract.isExpired() == true al invocar edición.
- Consecuencia: Se rechaza operación y se lanza InvalidContractStatusException.
- Error asociado: ERR_CONTRACT_NOT_EDITABLE

**RN-CONTRACT-003**
- Descripción: Solo puede suspenderse si está en estado ACTIVE.
- Condición: Contract.status != ACTIVE al invocar suspend().
- Consecuencia: Se rechaza operación con motivo obligatorio.
- Error asociado: ERR_CONTRACT_CANNOT_SUSPEND

**RN-CONTRACT-004**
- Descripción: No puede reactivarse si está vencido.
- Condición: Contract.status == SUSPENDED && Contract.isExpired() == true al invocar reactivate().
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_CONTRACT_EXPIRED_CANNOT_REACTIVATE

**RN-CONTRACT-005**
- Descripción: La extensión de vigencia solo permite fechas posteriores.
- Condición: newEndDate < Contract.endDate al invocar extendContract().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_CONTRACT_INVALID_EXTENSION

**RN-CONTRACT-006**
- Descripción: Debe tener tipo de cobertura válido.
- Condición: Contract.coverageType == null || coverageType.isBlank().
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_CONTRACT_MISSING_COVERAGE_TYPE

**RN-CONTRACT-007**
- Descripción: Alerta automática 30 días antes del vencimiento.
- Condición: Contract.getDaysRemaining() <= 30 && status == ACTIVE.
- Consecuencia: Sistema emite notificación automática.
- Evento generado: ContractNearExpirationEvent

**RN-CONTRACT-008**
- Descripción: La terminación requiere motivo obligatorio.
- Condición: Contract.terminate(reason) con reason == null || reason.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_CONTRACT_TERMINATION_REQUIRES_REASON

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - integración con facturación.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar contratos.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación cruzada con facturación.
- ADR-028 (Dominio): Conocimientos administrativos y contables.

---

## Eventos de Dominio
- ContractRegistered: Al crear un nuevo contrato.
- ContractSuspended: Al suspender un contrato activo.
- ContractReactivated: Al reactivar un contrato suspendido.
- ContractTerminated: Al finalizar anticipadamente un contrato.
- ContractExtended: Al extender la vigencia del contrato.
- ContractNearExpiration: 30 días antes del vencimiento.
- ContractExpired: Al detectar vencimiento automático.