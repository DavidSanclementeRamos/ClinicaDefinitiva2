# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Company (Empresa)

## Propósito
Representar la entidad legal o empresa en el sistema contable y administrativo, gestionando información fiscal, legal, de contacto y estado operativo. Este agregado protege la integridad jurídica y permite trazabilidad completa de la información corporativa para cumplimiento normativo colombiano.

---

## CREACIÓN
- Debe tener nombre válido (Name) y NIT único (Nit).
- Debe especificarse tipo de persona (NATURAL o JURÍDICA).
- Debe especificarse régimen tributario (SIMPLIFICADO, COMÚN).
- Fecha de constitución no puede ser futura.
- Fecha de constitución es obligatoria.
- Se asigna estado inicial ACTIVE por defecto.
- Representante legal es opcional pero recomendado.
- Debe registrar al menos un medio de contacto válido (email o teléfono).
- Dirección completa es obligatoria.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está en estado ACTIVE o SUSPENDED.
- No puede modificarse el NIT una vez registrado.
- No puede modificarse el tipo de persona sin justificación legal.
- Cambios en información fiscal requieren estado editable.
- Cambios en representante legal deben registrar auditoría.
- Fecha de constitución no puede modificarse retroactivamente.
- Cambios sensibles deben registrar fecha, responsable y motivo.

---

## DESACTIVACIÓN / REACTIVACIÓN
- No puede desactivarse si tiene contratos activos.
- No puede desactivarse si tiene movimientos contables del mes actual.
- Una empresa inactiva no puede reactivarse sin proceso formal.
- Debe registrar motivo obligatorio de desactivación.
- La eliminación física está prohibida; se maneja como estado lógico.

---

## OPERACIONES DE DOMINIO
- updateContactInformation() → Actualiza dirección, teléfono, email, representante legal.
- updateTaxInformation() → Actualiza NIT, régimen tributario, tipo de persona.
- updateStatus() → Cambia estado con validaciones de transición.
- ensureEditable() → Valida que el estado permita modificaciones.
- validateIncorporationDate() → Verifica que fecha de constitución sea válida.

---

## INVARIANTES GLOBALES
- Una empresa activa debe tener NIT, nombre y dirección válidos.
- No puede haber dos empresas con el mismo NIT.
- La fecha de constitución nunca puede ser futura.
- Una empresa inactiva no puede tener movimientos contables nuevos.
- El régimen tributario debe ser coherente con el tipo de persona.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado, información fiscal y representante legal.
- Se puede emitir un Outcome al intentar operaciones sobre empresa inactiva.
- Sistema de alertas para cambios en representante legal o régimen tributario.
- Auditoría completa de modificaciones con fecha y responsable.
- Integración con DIAN para validación de NIT.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de empresa sea coherente, legalmente válido y trazable. Protegen la integridad fiscal, evitan inconsistencias jurídicas y permiten auditar cada decisión relevante. El modelo está listo para integrarse con facturación electrónica DIAN, reportes contables y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-COMPANY-001**
- Descripción: Debe tener NIT único y válido.
- Condición: Company.taxIdentificationNumber == null al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_COMPANY_MISSING_TAX_ID

**RN-COMPANY-002**
- Descripción: Fecha de constitución no puede ser futura.
- Condición: Company.incorporationDate > LocalDate.now().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_COMPANY_FUTURE_INCORPORATION_DATE

**RN-COMPANY-003**
- Descripción: Solo puede editarse si está en estado editable (ACTIVE o SUSPENDED).
- Condición: Company.status == INACTIVE al invocar updateContactInformation().
- Consecuencia: Se rechaza operación con InvalidCompanyStatusException.
- Error asociado: ERR_COMPANY_NOT_EDITABLE

**RN-COMPANY-004**
- Descripción: No puede reactivarse sin proceso formal.
- Condición: Company.status == INACTIVE && intentar cambio directo a ACTIVE.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_COMPANY_CANNOT_REACTIVATE_DIRECTLY

**RN-COMPANY-005**
- Descripción: Tipo de persona es obligatorio.
- Condición: Company.typePerson == null al invocar creación.
- Consecuencia: Se rechaza operación con InvalidCompanyException.
- Error asociado: ERR_COMPANY_MISSING_PERSON_TYPE

**RN-COMPANY-006**
- Descripción: No puede modificarse el NIT una vez registrado.
- Condición: Intento de modificar taxIdentificationNumber después de creación.
- Consecuencia: Se rechaza operación (campo inmutable).
- Error asociado: ERR_COMPANY_CANNOT_MODIFY_TAX_ID

**RN-COMPANY-007**
- Descripción: Fecha de constitución es obligatoria.
- Condición: Company.incorporationDate == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_COMPANY_MISSING_INCORPORATION_DATE

**RN-COMPANY-008**
- Descripción: Debe tener al menos un medio de contacto válido.
- Condición: Company.email == null && Company.phoneNumber == null.
- Consecuencia: Se rechaza operación de creación.
- Error asociado: ERR_COMPANY_MISSING_CONTACT

---

## Relación con ADRs
- ADR-13 (Arquitectura): Plan de cuentas y asientos contables - Company como entidad propietaria.
- ADR-28 (Dominio): Conocimientos administrativos y contables colombianos.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar empresas.
- ADR-32 (Dominio): Reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas - validación cruzada con contratos y movimientos.

---

## Eventos de Dominio
- CompanyRegistered: Al crear una nueva empresa.
- CompanyContactUpdated: Al actualizar información de contacto.
- CompanyTaxInformationUpdated: Al actualizar información fiscal.
- CompanyStatusChanged: Al cambiar estado operativo.
- CompanyLegalRepresentativeChanged: Al cambiar representante legal.
- CompanyDeactivated: Al desactivar la empresa.

---

## Validaciones Específicas Colombia
- NIT debe cumplir formato colombiano con dígito de verificación.
- Régimen tributario debe ser SIMPLIFICADO o COMÚN según normativa DIAN.
- Tipo de persona debe ser NATURAL o JURÍDICA según Código de Comercio.
- Dirección debe incluir ciudad/municipio colombiano válido.
- Representante legal obligatorio para personas jurídicas.