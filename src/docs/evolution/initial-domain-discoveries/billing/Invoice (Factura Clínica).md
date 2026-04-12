# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Invoice (Factura Clínica)

## Propósito
Representar una factura por servicios odontológicos prestados, asegurando cumplimiento normativo colombiano (DIAN, resolución de facturación), trazabilidad de cobros, coherencia entre servicios prestados y montos facturados, y protección contra facturas inconsistentes o con tarifas vencidas. Este agregado es crítico para la sostenibilidad financiera de la clínica y el cumplimiento tributario.

---

## CREACIÓN
- Debe tener un paciente (PatientId) válido asociado.
- Debe tener un profesional/clínica (DentistId/ProviderId) que emite la factura.
- Debe especificar tipo de pagador (EPS, aseguradora, particular).
- Si el pagador es EPS, debe tener ContractId asociado (convenio vigente).
- Debe registrar fecha de emisión (issuedAt) y fecha de vencimiento (dueDate).
- La fecha de vencimiento debe ser posterior a la fecha de emisión.
- Debe tener moneda válida (COP para Colombia).
- No puede crearse sin al menos un ítem válido (InvoiceItem).
- Todos los ítems deben tener la misma moneda de la factura.
- Estado inicial obligatorio: DRAFT.
- Subtotal, tax y total se calculan automáticamente al agregar ítems.
- No puede tener total negativo o cero tras agregar ítems.
- Se registra automáticamente fecha de creación (createdAt).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse en estado DRAFT o PENDING.
- No puede editarse si está en estado PAID o CANCELLED.
- No puede agregarse ítems si está en estado PAID o CANCELLED.
- No puede modificarse el paciente tras emisión (transición a PENDING).
- No puede modificarse el pagador tras emisión.
- Puede actualizarse información de contacto del paciente/pagador antes de emisión.
- Cada cambio recalcula subtotal, impuestos y total automáticamente.
- Se actualiza automáticamente fecha de última modificación (updatedAt).
- Cambios sensibles deben registrar responsable y motivo.

---

## VALIDACIONES ANTES DE EMISIÓN (DRAFT → PENDING)
- Debe tener al menos un ítem válido (quantity > 0, unitPrice > 0).
- Todos los ítems deben tener tarifas vigentes (Rate.isValidAt(issuedAt)).
- Todos los servicios facturados deben estar activos (ProvidedService.status == ACTIVE).
- Subtotal debe ser > 0.
- Total debe ser > 0.
- Tax debe ser >= 0 (puede ser 0 si exento).
- Si pagador es EPS, debe validar existencia de contrato vigente.
- Si requiere autorización, debe tener número de autorización registrado.
- No puede emitirse si hay ítems con tarifas vencidas.
- No puede emitirse sin información completa del pagador.

---

## EMISIÓN (Transición DRAFT → PENDING)
- Solo puede emitirse desde estado DRAFT.
- Debe pasar todas las validaciones previas.
- Se asigna número de factura consecutivo (resolución DIAN en Colombia).
- Se crea snapshot inmutable de todos los ítems (unitPrice no cambia tras emisión).
- Se registra fecha de emisión definitiva.
- Se notifica al paciente/pagador (email/SMS).
- Se genera PDF con formato legal (resolución DIAN).
- Estado cambia a PENDING (pendiente de pago).
- Tras emisión, no puede modificarse paciente, pagador, ítems ni montos.

---

## CANCELACIÓN
- Solo puede cancelarse desde estado DRAFT o PENDING.
- No puede cancelarse si está en estado PAID.
- Debe registrar motivo obligatorio de cancelación (mínimo 10 caracteres).
- Debe registrar responsable de la cancelación.
- Si está en PENDING, requiere autorización gerencial.
- Cancelación de factura emitida requiere nota crédito (cumplimiento DIAN).
- Estado final: CANCELLED (inmutable).
- Se notifica al paciente/pagador.
- Se registra fecha de cancelación.

---

## PAGO (Transición PENDING → PAID)
- Solo puede marcarse como pagada desde estado PENDING.
- Debe tener al menos un registro de pago (Payment) asociado.
- El monto total de pagos debe ser >= total de la factura.
- Si hay sobrepago, se registra como saldo a favor.
- Se registra fecha de pago.
- Se actualiza estado a PAID.
- Estado PAID es final (no puede cancelarse ni editarse).
- Se genera recibo de pago (comprobante).
- Se notifica al paciente (confirmación de pago).

---

## OPERACIONES DE DOMINIO
- addItem(InvoiceItem item) → Agrega ítem validando coherencia.
- replaceAllItems(List<InvoiceItem> items) → Reemplaza ítems con revalidación.
- recalcTotals() → Recalcula subtotal, tax, total.
- validateBeforeEmit() → Ejecuta todas las validaciones previas a emisión.
- markPending() → Transición DRAFT → PENDING con validaciones.
- markPaid(Payment payment) → Transición PENDING → PAID.
- cancel(String reason) → Cancelación con motivo obligatorio.
- canAddItem() → Verifica si el estado permite agregar ítems.
- getTotalWithTax() → Retorna total incluyendo impuestos.
- hasExpiredRates() → Verifica si algún ítem tiene tarifa vencida.
- requiresAuthorization() → Indica si requiere autorización previa (EPS).

---

## INVARIANTES GLOBALES
- Una factura válida siempre tiene al menos un ítem.
- Subtotal + Tax = Total (invariante matemático).
- Todos los ítems deben tener la misma moneda que la factura.
- No puede tener total negativo.
- Estado PAID no puede cambiar a ningún otro estado.
- Estado CANCELLED no puede cambiar a ningún otro estado.
- Fecha de vencimiento siempre posterior a fecha de emisión.
- Una factura emitida (PENDING o PAID) no puede modificar paciente, pagador ni ítems.
- Todos los ítems deben tener cantidad > 0.
- Si pagador es EPS, ContractId no puede ser null.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado con fecha, hora y responsable.
- Se registra cada adición/eliminación de ítems.
- Se crea snapshot inmutable al emitir (precio y cantidad no cambian).
- Se registra número de factura consecutivo (resolución DIAN).
- Se mantiene histórico de pagos asociados.
- Sistema emite alertas si se intenta facturar con tarifa vencida.
- Se registra motivo obligatorio en cancelaciones.
- Auditoría completa del ciclo de vida de la factura.
- Se mantiene PDF inmutable de factura emitida.

---

## Justificación Semántica
Estas reglas aseguran cumplimiento normativo colombiano (DIAN, resolución de facturación electrónica), protegen contra facturas con tarifas vencidas que generan reclamaciones, garantizan coherencia financiera (subtotal + tax = total), evitan modificaciones tras emisión que invaliden auditorías, aseguran trazabilidad para cobro a EPSs y aseguradoras, protegen integridad referencial con servicios prestados, y permiten auditorías tributarias confiables.

---

## Reglas Descubiertas (formato estandarizado)

**RN-INVOICE-001**
- Descripción: Debe tener al menos un ítem válido antes de emitir.
- Condición: Invoice.items.isEmpty() al invocar markPending().
- Consecuencia: Se rechaza emisión y se registra Outcome.
- Error asociado: ERR_INVOICE_NO_ITEMS

**RN-INVOICE-002**
- Descripción: Total debe ser mayor a cero antes de emitir.
- Condición: Invoice.total.isNegativeOrZero() al invocar validateBeforeEmit().
- Consecuencia: Se rechaza emisión y se registra Outcome.
- Error asociado: ERR_INVOICE_ZERO_TOTAL

**RN-INVOICE-003**
- Descripción: No puede emitirse con ítems que tengan tarifas vencidas.
- Condición: InvoiceItem.rate.isValidAt(issuedAt) == false al invocar validateBeforeEmit().
- Consecuencia: Se rechaza emisión y se registra Outcome.
- Error asociado: ERR_INVOICE_EXPIRED_RATE

**RN-INVOICE-004**
- Descripción: Solo puede agregarse ítems en estado DRAFT o PENDING.
- Condición: Invoice.status not in [DRAFT, PENDING] al invocar addItem().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_INVOICE_CANNOT_ADD_ITEM

**RN-INVOICE-005**
- Descripción: No puede cancelarse si está en estado PAID.
- Condición: Invoice.status == PAID al invocar cancel().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_INVOICE_CANNOT_CANCEL_PAID

**RN-INVOICE-006**
- Descripción: Fecha de vencimiento debe ser posterior a fecha de emisión.
- Condición: Invoice.dueDate <= Invoice.issuedAt al invocar creación.
- Consecuencia: Se rechaza creación.
- Error asociado: ERR_INVOICE_INVALID_DUE_DATE

**RN-INVOICE-007**
- Descripción: Si pagador es EPS, debe tener contrato asociado.
- Condición: Invoice.payer.startsWith("EPS") && Invoice.contractId == null al invocar validateBeforeEmit().
- Consecuencia: Se rechaza emisión.
- Error asociado: ERR_INVOICE_MISSING_CONTRACT

**RN-INVOICE-008**
- Descripción: Todos los ítems deben tener la misma moneda.
- Condición: InvoiceItem.currency != Invoice.currency al invocar addItem().
- Consecuencia: Se rechaza adición de ítem.
- Error asociado: ERR_INVOICE_CURRENCY_MISMATCH

**RN-INVOICE-009**
- Descripción: Cancelación requiere motivo obligatorio (mínimo 10 caracteres).
- Condición: cancel(reason) con reason == null || reason.length() < 10.
- Consecuencia: Se rechaza cancelación.
- Error asociado: ERR_INVOICE_CANCELLATION_REQUIRES_REASON

**RN-INVOICE-010**
- Descripción: No puede modificarse tras emisión (estado PENDING o PAID).
- Condición: Invoice.status in [PENDING, PAID] al intentar replaceAllItems().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_INVOICE_IMMUTABLE_AFTER_EMISSION

**RN-INVOICE-011**
- Descripción: Subtotal + Tax debe igualar Total (invariante matemático).
- Condición: Invoice.subtotal.add(Invoice.tax) != Invoice.total después de recalcTotals().
- Consecuencia: Se lanza IllegalStateException (corrupción de datos).
- Error asociado: ERR_INVOICE_TOTAL_MISMATCH

**RN-INVOICE-012**
- Descripción: Transición PENDING → PAID requiere pago registrado.
- Condición: Invoice.payments.isEmpty() || Invoice.payments.sum() < Invoice.total al invocar markPaid().
- Consecuencia: Se rechaza transición.
- Error asociado: ERR_INVOICE_UNPAID

**RN-INVOICE-013**
- Descripción: Número de factura debe ser consecutivo (resolución DIAN).
- Condición: Invoice.invoiceNumber no es consecutivo al invocar markPending().
- Consecuencia: Se rechaza emisión (requiere resolución DIAN válida).
- Error asociado: ERR_INVOICE_INVALID_NUMBER_SEQUENCE

**RN-INVOICE-014**
- Descripción: Servicios facturados deben estar activos.
- Condición: InvoiceItem.service.status != ACTIVE al invocar validateBeforeEmit().
- Consecuencia: Se rechaza emisión.
- Error asociado: ERR_INVOICE_INACTIVE_SERVICE

**RN-INVOICE-015**
- Descripción: Paciente y proveedor no pueden ser null.
- Condición: Invoice.patientId == null || Invoice.providerId == null al invocar creación.
- Consecuencia: Se rechaza creación.
- Error asociado: ERR_INVOICE_MISSING_REQUIRED_FIELDS

---

## Relación con ADRs
- [ADR-(Arquitectura)-06-Separación de Facturación y Pagos en módulos independientes.md](../../../architecture/adr/ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md)
- [ADR-(Dominio)-02-Implementación de reglas de negocio.md](../../../architecture/decisions/ADR-%28Dominio%29-02-Implementaci%C3%B3n%20de%20reglas%20de%20negocio.md)
- [ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md](../../../architecture/decisions/billing/ADR-%28Facturaci%C3%B3n%29-01-Validaci%C3%B3n%20de%20Tarifas%20Vigentes%20al%20Momento%20de%20Facturar.md)
- [ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md](../../../architecture/decisions/billing/ADR-%28Facturaci%C3%B3n%29-02-Snapshot%20Inmutable%20de%20Precios%20en%20Facturaci%C3%B3n.md)
- [ADR-(Facturación)-03-Cumplimiento Normativo DIAN Colombia.md](../../../architecture/decisions/billing/ADR-%28Facturaci%C3%B3n%29-03-Cumplimiento%20Normativo%20DIAN%20Colombia.md)
---

## Eventos de Dominio
- InvoiceCreated: Al crear nueva factura en DRAFT.
- InvoiceItemAdded: Al agregar ítem a factura.
- InvoiceEmitted: Al transicionar DRAFT → PENDING (emisión oficial).
- InvoicePaid: Al transicionar PENDING → PAID.
- InvoiceCancelled: Al cancelar factura con motivo.
- InvoiceTotalsRecalculated: Al recalcular montos.
- InvoiceOverdue: Factura vencida sin pago (fecha actual > dueDate).
- InvoicePaymentReceived: Al registrar pago parcial o total.

---

## Contexto Colombiano - Normativa DIAN

**Resolución de Facturación Electrónica (Decreto 358 de 2020)**
- Factura electrónica obligatoria para prestadores de salud.
- Numeración consecutiva autorizada por DIAN.
- Firma digital/electrónica obligatoria.
- Transmisión a DIAN en tiempo real.
- Conservación de facturas por 5 años.

**Facturación a EPS (Entidades Promotoras de Salud)**
```java
PayerTypes Colombia:
- "EPS" → Facturación con contrato vigente (ej. EPS Sura, Salud Total)
- "PARTICULAR" → Pago directo del paciente
- "ASEGURADORA" → Seguros privados (ej. Seguros Bolívar)
- "PREPAGADA" → Medicina prepagada (ej. Colsanitas, Coomeva)
- "ARL" → Accidente laboral (ej. Positiva, Sura ARL)
```

**Requisitos Legales Mínimos (Factura Válida DIAN)**
- Número de factura consecutivo autorizado.
- NIT del prestador (clínica/profesional).
- Identificación del paciente (CC, TI, CE).
- Descripción detallada del servicio (código CUPS).
- Valor unitario y total.
- Discriminación de impuestos (IVA si aplica).
- Fecha de emisión y vencimiento.
- Forma de pago.

**Tarifas SOAT y EPS**
- Tarifas ISS 2001 (manual tarifario de salud).
- Tarifas SOAT para accidentes de tránsito.
- Contratos específicos con cada EPS.
- Rate debe tener validez temporal (vigencia contractual).

---

## Integración con Otros Módulos

**ProvidedService (Módulo Services)**
- Invoice valida que servicio esté activo antes de facturar.
- InvoiceItem.serviceCode referencia ProvidedService.code.
- Snapshot de tarifa al momento de facturar (inmutable).

**Appointment (Módulo Schedule)**
- Invoice se genera tras Appointment.status == COMPLETED.
- InvoiceItem.performedAt referencia Appointment.start.
- Validación: no facturar cita no completada.

**Patient (Módulo Actor)**
- Invoice.patientId referencia Patient.id.
- Información de contacto para envío de factura.

**Rate (Módulo Billing)**
- InvoiceItem usa Rate vigente al momento de facturar.
- Validación crítica: Rate.isValidAt(invoiceDate).

**Payment (Módulo Payments - futuro)**
- Invoice.PAID requiere Payment.amount >= Invoice.total.
- Evento InvoicePaid ↔ PaymentReceived.

---

## Value Objects Involucrados

**InvoiceId (Identificador de Factura)**
- UUID único por factura.
- Inmutable.
- Permite trazabilidad entre sistemas.

**InvoiceStatus (Estado de Factura - VO con Transiciones)**
- Posibles estados: DRAFT, PENDING, PAID, CANCELLED.
- Controla transiciones válidas:
    - DRAFT → PENDING, CANCELLED
    - PENDING → PAID, CANCELLED
    - PAID → (final, inmutable)
    - CANCELLED → (final, inmutable)

**Price (Monto/Tarifa)**
- Representa subtotal, tax, total.
- Incluye amount (BigDecimal) y currency (String).
- Validaciones: amount >= 0, currency válida.
- Operaciones: add, subtract, multiply, compare.

**ContractId (Identificador de Contrato)**
- Referencia a convenio con EPS/aseguradora.
- Obligatorio si payer es EPS.
- Validación: contrato debe estar vigente.

**TaxRate (Porcentaje de Impuesto - VO Nuevo)**
- Colombia: IVA 19% (exento para salud en general, aplica para estética).
- Validación: 0% <= taxRate <= 100%.
- Inmutable.

**PaymentTerms (Términos de Pago - VO Nuevo)**
- Valores: IMMEDIATE, NET_30, NET_60, NET_90.
- Define dueDate automáticamente.
- Inmutable.

---

## Ejemplo de Uso

```java
// Crear factura para paciente particular
InvoiceId invoiceId = InvoiceId.generate();
PatientId patientId = PatientId.of("patient-123");
DentistId providerId = DentistId.of("dentist-456");

Invoice invoice = new Invoice(
    invoiceId,
    patientId,
    providerId,
    LocalDateTime.now(), // issuedAt
    LocalDateTime.now().plusDays(30), // dueDate (NET_30)
    "COP",
    "PARTICULAR",
    null, // contractId (no aplica para particulares)
    "Factura por servicios odontológicos"
);

// Agregar ítem (limpieza dental)
ProvidedService cleaningService = serviceRepository.findByCode("890101");
Rate currentRate = rateRepository.findActiveRateFor(cleaningService, "PARTICULAR");

InvoiceItem item1 = new InvoiceItem(
    InvoiceItemId.generate(),
    "890101", // código CUPS
    "Profilaxis y limpieza dental",
    1, // quantity
    currentRate.getAmount(), // unitPrice
    "COP",
    currentRate.getId(),
    LocalDateTime.now(), // performedAt
    providerId,
    invoiceId
);

invoice.addItem(item1);
// Recalcula automáticamente: subtotal, tax, total

// Validar antes de emitir
try {
    invoice.validateBeforeEmit();
    invoice.markPending(); // Transición DRAFT → PENDING
} catch (BusinessRuleViolationException e) {
    // Lanza error si hay ítems con tarifas vencidas
    System.err.println("No se puede emitir: " + e.getMessage());
}

// Registrar pago
Payment payment = new Payment(
    PaymentId.generate(),
    invoice.getId(),
    invoice.getTotal(),
    "CASH",
    LocalDateTime.now()
);
invoice.markPaid(payment); // Transición PENDING → PAID
```

---