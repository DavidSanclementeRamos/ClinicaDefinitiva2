# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: ThirdParties (Terceros)

## Propósito
Representar terceros en el sistema contable (proveedores, clientes, empleados), gestionando información de contacto, clasificación y estado operativo. Este agregado protege la integridad de las relaciones comerciales y permite trazabilidad completa para transacciones contables y administrativas.

---

## CREACIÓN
- Debe tener nombre válido (Name).
- Tipo de documento es obligatorio (CC, NIT, CE, Pasaporte).
- Número de documento es obligatorio y único.
- Número de documento debe tener entre 5 y 20 caracteres.
- Tipo de tercero es obligatorio (PROVEEDOR, CLIENTE, EMPLEADO, OTRO).
- Se asigna estado inicial ACTIVE por defecto.
- CompanyId asociado es obligatorio.
- Dirección, teléfono y email son opcionales pero recomendados.
- Número de documento solo acepta caracteres alfanuméricos.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está ACTIVE.
- No puede modificarse el número de documento una vez registrado.
- No puede modificarse el tipo de documento.
- Puede actualizarse nombre, dirección, teléfono, email.
- No puede modificarse el tipo de tercero sin justificación.
- Cambios sensibles deben registrar auditoría con fecha y responsable.

---

## ACTIVACIÓN / INACTIVACIÓN
- Puede activarse si está inactivo.
- Puede inactivarse solo con motivo obligatorio.
- No puede inactivarse si tiene movimientos contables del mes actual.
- No puede inactivarse si tiene facturas pendientes.
- Terceros inactivos no pueden realizar nuevas transacciones.
- La eliminación física está prohibida; se maneja como estado lógico.

---

## OPERACIONES DE DOMINIO
- updateContactInformation() → Actualiza nombre, dirección, teléfono, email.
- activate() → Activa tercero para permitir operaciones.
- inactivate(motivo) → Inactiva tercero con motivo obligatorio.
- canPerformTransactions() → Verifica si puede realizar transacciones.
- isSupplier() → Verifica si es proveedor.
- isCustomer() → Verifica si es cliente.
- isEmployee() → Verifica si es empleado.

---

## INVARIANTES GLOBALES
- Un tercero activo debe tener nombre, tipo documento y número documento válidos.
- No puede haber dos terceros con el mismo número de documento en la misma compañía.
- El número de documento debe tener entre 5 y 20 caracteres alfanuméricos.
- Un tercero inactivo no puede realizar transacciones nuevas.
- El tipo de tercero debe ser coherente con las operaciones que realiza.
- Un proveedor inactivo no puede tener pagos pendientes.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado, activación, inactivación.
- Se registra motivo obligatorio al inactivar.
- Sistema emite alertas al intentar transacciones con tercero inactivo.
- Auditoría completa de modificaciones con fecha y responsable.
- Integración con asientos contables y movimientos financieros.

---

## Justificación Semántica
Estas reglas aseguran que el catálogo de terceros sea coherente, protegen la integridad de relaciones comerciales, evitan transacciones inválidas y permiten auditar cada decisión. El modelo está listo para integrarse con contabilidad, facturación, pagos y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-THIRDPARTIES-001**
- Descripción: Número de documento debe tener entre 5 y 20 caracteres.
- Condición: documentNumber.length() < 5 || documentNumber.length() > 20.
- Consecuencia: Se rechaza operación con InvalidThirdPartiesException.
- Error asociado: ERR_THIRD_PARTY_INVALID_DOCUMENT_LENGTH

**RN-THIRDPARTIES-002**
- Descripción: Tipo de documento es obligatorio.
- Condición: ThirdParties.typeDocument == null || typeDocument.isBlank().
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_THIRD_PARTY_MISSING_DOCUMENT_TYPE

**RN-THIRDPARTIES-003**
- Descripción: Número de documento es obligatorio y único.
- Condición: ThirdParties.documentNumber == null || documentNumber.isBlank().
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_THIRD_PARTY_MISSING_DOCUMENT_NUMBER

**RN-THIRDPARTIES-004**
- Descripción: Tipo de tercero es obligatorio.
- Condición: ThirdParties.typeThirdParties == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_THIRD_PARTY_MISSING_TYPE

**RN-THIRDPARTIES-005**
- Descripción: Solo puede editarse si está activo.
- Condición: ThirdParties.active == false al invocar updateContactInformation().
- Consecuencia: Se rechaza operación con InvalidThirdPartiesException.
- Error asociado: ERR_THIRD_PARTY_NOT_EDITABLE

**RN-THIRDPARTIES-006**
- Descripción: Inactivación requiere motivo obligatorio.
- Condición: inactivate(reason) con reason == null || reason.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_THIRD_PARTY_INACTIVATION_REQUIRES_REASON

**RN-THIRDPARTIES-007**
- Descripción: No puede modificarse el número de documento una vez registrado.
- Condición: Intento de modificar documentNumber después de creación.
- Consecuencia: Campo inmutable, operación rechazada.
- Error asociado: ERR_THIRD_PARTY_CANNOT_MODIFY_DOCUMENT

**RN-THIRDPARTIES-008**
- Descripción: Número de documento debe ser único por compañía.
- Condición: Existe otro ThirdParties con mismo documentNumber y companyId.
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_THIRD_PARTY_DUPLICATE_DOCUMENT

**RN-THIRDPARTIES-009**
- Descripción: Número de documento solo acepta caracteres alfanuméricos.
- Condición: documentNumber contiene caracteres especiales no permitidos.
- Consecuencia: Se rechaza operación (validación en constructor).
- Error asociado: ERR_THIRD_PARTY_INVALID_DOCUMENT_FORMAT

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - terceros en movimientos.
- ADR-28 (Dominio): Conocimientos administrativos y contables.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar terceros.
- ADR-32 (Dominio): Reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas - validación de transacciones.

---

## Eventos de Dominio
- ThirdPartyRegistered: Al crear un nuevo tercero.
- ThirdPartyContactUpdated: Al actualizar información de contacto.
- ThirdPartyActivated: Al activar tercero inactivo.
- ThirdPartyInactivated: Al inactivar tercero.
- ThirdPartyTransactionAttempted: Al intentar transacción con tercero inactivo (auditoría).

---

## Tipos de Tercero (Colombia)

**PROVEEDOR (Supplier)**
- Personas o empresas que suministran bienes/servicios.
- Se registran en cuentas de pasivo (2205 - Proveedores).
- Operaciones: compras, pagos, cuentas por pagar.

**CLIENTE (Customer)**
- Personas o empresas que adquieren servicios.
- Se registran en cuentas de activo (1305 - Clientes).
- Operaciones: ventas, cobros, cuentas por cobrar.

**EMPLEADO (Employee)**
- Personal de nómina de la organización.
- Se registran en cuentas de gasto (5105 - Gastos de personal).
- Operaciones: nómina, prestaciones, retenciones.

**OTRO (Other)**
- Terceros que no encajan en categorías anteriores.
- Pueden ser: socios, entidades gubernamentales, bancos.
- Operaciones: aportes, impuestos, préstamos.

---

## Tipos de Documento (Colombia)

- **CC**: Cédula de Ciudadanía (colombianos).
- **NIT**: Número de Identificación Tributaria (empresas).
- **CE**: Cédula de Extranjería (extranjeros residentes).
- **Pasaporte**: Para extranjeros no residentes.
- **TI**: Tarjeta de Identidad (menores de edad).
- **RUT**: Registro Único Tributario (DIAN).