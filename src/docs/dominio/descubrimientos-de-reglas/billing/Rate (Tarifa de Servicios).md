# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Rate (Tarifa de Servicios)

## Propósito
Representar la tarifa aplicable a un servicio odontológico según tipo de pagador (EPS, particular, aseguradora) y vigencia contractual. Este agregado gestiona el precio que se cobrará por cada servicio, asegura coherencia entre contratos y tarifas, protege contra facturas con tarifas vencidas, y permite trazabilidad de cambios tarifarios para auditorías y reclamaciones ante EPSs.

---

## CREACIÓN
- Debe estar asociada a un servicio válido (ProvidedService).
- Debe especificar tipo de pagador (private, EPS, insurer, ARL, PREPAGADA).
- Si el tipo de pagador es EPS/PREPAGADA, debe tener ContractId asociado.
- Debe tener monto (amount) mayor a cero.
- Debe tener moneda válida (COP para Colombia).
- Debe especificar fecha de inicio de vigencia (valid_from).
- La fecha de fin de vigencia (valid_to) puede ser null (vigencia indefinida).
- Si valid_to no es null, debe ser posterior a valid_from.
- Estado inicial por defecto: is_active = true.
- No pueden existir dos tarifas activas simultáneas para mismo servicio + pagador + contrato.
- Se registra automáticamente fecha de creación.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está activa (is_active = true).
- No puede modificarse el servicio asociado (inmutable).
- No puede modificarse el tipo de pagador (inmutable).
- No puede modificarse el contrato asociado (inmutable).
- Puede ajustarse el monto (amount) con justificación y auditoría.
- Cambios de monto requieren registrar motivo y responsable.
- Puede extenderse la vigencia (valid_to) si aún no ha vencido.
- No puede acortarse la vigencia si ya tiene facturas emitidas con esa tarifa.
- Cambios sensibles deben registrar fecha, responsable y motivo.

---

## DESACTIVACIÓN
- Solo puede desactivarse si no tiene facturas pendientes de emisión.
- Debe registrar motivo obligatorio de desactivación.
- La desactivación es lógica (is_active = false), no eliminación física.
- Una tarifa desactivada no puede reactivarse (crear nueva con nueva vigencia).
- Debe notificar a sistemas dependientes (agenda, facturaciÃ³n).
- Tarifas desactivadas mantienen su información histórica inmutable.

---

## VALIDACIÓN DE VIGENCIA
- isValidAt(fechaHora) → Verifica si la tarifa está vigente en esa fecha.
- Una tarifa es vigente si:
    - is_active = true
    - fechaHora >= valid_from
    - valid_to == null OR fechaHora <= valid_to
- Facturación solo puede usar tarifas vigentes al momento de emisión.
- Citas no pueden agendarse con tarifas que vencerán antes de la fecha de la cita.

---

## OPERACIONES DE DOMINIO
- isValidAt(LocalDateTime when) → Valida vigencia temporal.
- overlapsVigenciaCon(Rate otra) → Detecta conflictos de vigencia (mismo servicio + pagador).
- canBeUsedForInvoicing(LocalDateTime invoiceDate) → Valida si puede usarse en factura.
- hasActiveInvoices() → Verifica si tiene facturas activas que impiden cambios.
- getEffectiveDateRange() → Retorna rango de fechas efectivo.
- compareRateTo(Rate otra) → Compara monto con otra tarifa.
- adjustAmount(Money newAmount, String justification) → Ajusta monto con auditoría.
- extendValidityTo(LocalDateTime newValidTo) → Extiende vigencia.
- deactivate(String reason) → Desactiva con motivo obligatorio.

---

## INVARIANTES GLOBALES
- Una tarifa válida siempre tiene monto > 0.
- valid_to debe ser posterior a valid_from (si no es null).
- No pueden coexistir dos tarifas activas con vigencias solapadas para mismo servicio + pagador + contrato.
- Una tarifa activa (is_active = true) debe tener vigencia actual o futura.
- Si tipo de pagador es EPS/PREPAGADA, ContractId no puede ser null.
- El monto de la tarifa debe estar en la misma moneda del servicio.
- Una tarifa con valid_to < NOW() debe estar desactivada.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de monto con fecha, responsable y justificación.
- Se registra cada ajuste de vigencia.
- Se crea snapshot inmutable al usar en factura.
- Sistema emite alertas si se intenta facturar con tarifa vencida.
- Se registra motivo obligatorio en desactivaciones.
- Auditoría completa de cambios tarifarios.
- Se mantiene histórico de tarifas por servicio.

---

## Justificación Semántica
Estas reglas aseguran coherencia entre contratos y tarifas aplicadas, protegen contra facturas con tarifas vencidas que generan reclamaciones de EPSs, garantizan trazabilidad para auditorías y glosas, evitan conflictos de tarifas simultáneas que causan inconsistencias en cobro, permiten análisis histórico de cambios de precios, y aseguran cumplimiento de convenios contractuales con pagadores (EPSs, aseguradoras).

---

## Reglas Descubiertas (formato estandarizado)

**RN-RATE-001**
- Descripción: El monto debe ser mayor a cero.
- Condición: Rate.amount <= 0 al invocar creación.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_RATE_INVALID_AMOUNT

**RN-RATE-002**
- Descripción: valid_to debe ser posterior a valid_from.
- Condición: Rate.valid_to != null && Rate.valid_to <= Rate.valid_from al invocar creación.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_RATE_INVALID_VALIDITY_RANGE

**RN-RATE-003**
- Descripción: Solo puede facturarse con tarifa vigente.
- Condición: !Rate.isValidAt(invoiceDate) al intentar usar en InvoiceItem.
- Consecuencia: Se rechaza facturación.
- Error asociado: ERR_RATE_EXPIRED

**RN-RATE-004**
- Descripción: No pueden coexistir dos tarifas activas con vigencias solapadas.
- Condición: exists(serviceId, payerType, contractId) con vigencias solapadas.
- Consecuencia: Se rechaza creación de nueva tarifa.
- Error asociado: ERR_RATE_VALIDITY_CONFLICT

**RN-RATE-005**
- Descripción: Si pagador es EPS, debe tener contrato asociado.
- Condición: Rate.payer_type == "EPS" && Rate.contract_id == null al invocar creación.
- Consecuencia: Se rechaza creación.
- Error asociado: ERR_RATE_MISSING_CONTRACT

**RN-RATE-006**
- Descripción: No puede desactivarse si tiene facturas pendientes.
- Condición: Rate.hasActiveInvoices() == true al invocar deactivate().
- Consecuencia: Se rechaza desactivación.
- Error asociado: ERR_RATE_HAS_ACTIVE_INVOICES

**RN-RATE-007**
- Descripción: Desactivación requiere motivo obligatorio (mínimo 10 caracteres).
- Condición: deactivate(reason) con reason == null || reason.length() < 10.
- Consecuencia: Se rechaza desactivación.
- Error asociado: ERR_RATE_DEACTIVATION_REQUIRES_REASON

**RN-RATE-008**
- Descripción: Cambio de monto requiere justificación.
- Condición: adjustAmount(newAmount, justification) con justification == null || justification.isBlank().
- Consecuencia: Se rechaza ajuste.
- Error asociado: ERR_RATE_ADJUSTMENT_REQUIRES_JUSTIFICATION

**RN-RATE-009**
- Descripción: Solo puede editarse si está activa.
- Condición: Rate.is_active == false al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RATE_INACTIVE

**RN-RATE-010**
- Descripción: Servicio asociado debe estar activo al crear tarifa.
- Condición: Rate.service.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza creación.
- Error asociado: ERR_RATE_INACTIVE_SERVICE

**RN-RATE-011**
- Descripción: Moneda de la tarifa debe coincidir con moneda del servicio.
- Condición: Rate.currency != Rate.service.currency al invocar creación.
- Consecuencia: Se rechaza creación.
- Error asociado: ERR_RATE_CURRENCY_MISMATCH

**RN-RATE-012**
- Descripción: valid_from no puede ser fecha futura muy lejana (> 2 años).
- Condición: Rate.valid_from > LocalDateTime.now().plusYears(2) al invocar creación.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza.
- Error asociado: WARN_RATE_FUTURE_VALIDITY

**RN-RATE-013**
- Descripción: Tarifa vencida (valid_to < NOW) debe estar desactivada.
- Condición: Rate.valid_to < LocalDateTime.now() && Rate.is_active == true al validar.
- Consecuencia: Se marca automáticamente como inactiva.
- Error asociado: ERR_RATE_EXPIRED_BUT_ACTIVE

**RN-RATE-014**
- Descripción: No puede acortarse vigencia con facturas emitidas.
- Condición: Rate.hasInvoicesAfter(newValidTo) == true al intentar extendValidityTo().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RATE_CANNOT_SHORTEN_VALIDITY

**RN-RATE-015**
- Descripción: Nuevo monto no puede ser > 300% del monto anterior sin aprobación.
- Condición: newAmount > (oldAmount * 3.0) al invocar adjustAmount() sin aprobación gerencial.
- Consecuencia: Se rechaza ajuste o requiere aprobación.
- Error asociado: ERR_RATE_EXCESSIVE_ADJUSTMENT

---

## Relación con ADRs
- [ADR-(Arquitectura)-06-Separación de Facturación y Pagos en módulos independientes.md](../../../arquitetura/adr/ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md)
- [ADR-(Dominio)-02-Implementación de reglas de negocio.md](../../decisions/ADR-%28Dominio%29-02-Implementaci%C3%B3n%20de%20reglas%20de%20negocio.md)
- [ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md](../../decisions/billing/ADR-%28Facturaci%C3%B3n%29-01-Validaci%C3%B3n%20de%20Tarifas%20Vigentes%20al%20Momento%20de%20Facturar.md)
- [ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md](../../decisions/billing/ADR-%28Facturaci%C3%B3n%29-02-Snapshot%20Inmutable%20de%20Precios%20en%20Facturaci%C3%B3n.md)
- [ADR-(Facturación)-03-Cumplimiento Normativo DIAN Colombia.md](../../decisions/billing/ADR-%28Facturaci%C3%B3n%29-03-Cumplimiento%20Normativo%20DIAN%20Colombia.md)
---
---

## Eventos de Dominio
- RateCreated: Al crear nueva tarifa.
- RateAmountAdjusted: Al cambiar monto de tarifa.
- RateValidityExtended: Al extender valid_to.
- RateDeactivated: Al desactivar tarifa con motivo.
- RateExpired: Al llegar a valid_to (dispara desactivación automática).
- RateConflictDetected: Al detectar solapamiento de vigencias.
- RateUsedInInvoice: Al usar tarifa en factura (snapshot).

---

## Contexto Colombiano - Tarifas y Contratos

**Manual Tarifario ISS 2001**
- Base para cálculo de tarifas en salud.
- Tarifa = Valor UVR × Factor × Cantidad.
- UVR (Unidad de Valor Relativo) definida por servicio.
- Factor negociado en contrato con EPS.

**Tarifas SOAT (Accidentes de Tránsito)**
```java
PayerTypes:
- "SOAT" → Tarifas fijas definidas por Fasecolda.
- Cobertura limitada: atención inicial, cirugía básica.
- Sin necesidad de contrato (obligatorio por ley).
```

**Tarifas por Tipo de Pagador**
```java
private/PARTICULAR:
- Tarifas libres (definidas por clínica).
- Sin contrato requerido.
- Pago inmediato.

EPS (Contributivo/Subsidiado):
- Tarifas negociadas en contrato.
- Requiere autorización previa.
- Pago a 30-60 días (NET_30, NET_60).
- Ejemplo: EPS Sura, Salud Total, Nueva EPS.

PREPAGADA:
- Tarifas superiores a EPS (generalmente).
- Contrato con medicina prepagada.
- Autorización previa obligatoria.
- Ejemplo: Coomeva, Colsanitas.

ARL (Accidente Laboral):
- Tarifas ISS 2001 + porcentaje.
- Cobertura por evento laboral.
- Ejemplo: Positiva, Sura ARL.

ASEGURADORA:
- Seguros privados (pólizas individuales).
- Tarifas variables según póliza.
- Ejemplo: Seguros Bolívar, Liberty.
```

**Vigencias Típicas de Contratos**
```
EPS: 1-2 años (renovación anual).
PREPAGADA: 1 año (renovación automática).
PARTICULAR: vigencia indefinida (ajuste manual).
SOAT: vigencia indefinida (ajustada por Fasecolda).
```

**Ejemplo de Tarifas Colombia (2025)**
```
CONSULTA GENERAL:
- Particular: $80,000 - $150,000 COP
- EPS: $45,000 - $65,000 COP (ISS 2001 × 1.3)
- Prepagada: $100,000 - $180,000 COP

ORTODONCIA (BRACKETS):
- Particular: $4,000,000 - $8,000,000 COP
- EPS: NO cubierto (cosmético)
- Prepagada: $5,000,000 - $10,000,000 COP

EXTRACCIÓN SIMPLE:
- Particular: $120,000 - $200,000 COP
- EPS: $70,000 - $90,000 COP (ISS 2001)
- SOAT: $85,000 COP (tarifa fija)
```

---

## Integración con Otros Módulos

**ProvidedService (Módulo Services)**
- Rate referencia ProvidedService.id.
- Validación: servicio debe estar activo al crear tarifa.
- Moneda debe coincidir.

**Invoice (Módulo Billing)**
- InvoiceItem usa Rate.amount como unitPrice.
- Validación crítica: Rate.isValidAt(invoiceDate).
- Snapshot inmutable de tarifa al facturar.

**Contract (Módulo Administration - futuro)**
- Rate.contract_id referencia Contract.id.
- Validación: contrato debe estar vigente.
- Tarifa hereda vigencia de contrato (valid_from, valid_to).

---

## Value Objects Involucrados

**RateId (Identificador de Tarifa)**
- UUID único por tarifa.
- Inmutable.
- Permite trazabilidad.

**Money (Monto)**
- Representa tarifa (amount + currency).
- Validaciones: amount > 0, currency válida.
- Operaciones: compare, multiply, add.

**ContractId (Identificador de Contrato)**
- Referencia a convenio con EPS/aseguradora.
- Obligatorio si payer_type es EPS/PREPAGADA.
- Validación: contrato debe estar vigente.

**ValidityPeriod (Período de Vigencia - VO Nuevo)**
- Encapsula valid_from y valid_to.
- Validaciones: valid_to > valid_from.
- Métodos: isValidAt(), overlaps(), extend().

**PayerType (Tipo de Pagador - Enum)**
- Valores: PRIVATE, EPS, PREPAGADA, ARL, SOAT, INSURER.
- Define lógica de autorización requerida.
- Inmutable.

---

## Ejemplo de Uso

```java
// Crear tarifa para paciente particular (sin contrato)
RateId rateId = RateId.generate();
ProvidedService cleaningService = serviceRepository.findByCode("890101");
Money privateRate = Money.of(new BigDecimal("100000"), "COP");

Rate privateRate = new Rate(
    privateRate,
    rateId,
    cleaningService,
    "private", // payer_type
    null, // contract_id (no aplica)
    "COP",
    LocalDateTime.now(), // valid_from (vigencia inmediata)
    null, // valid_to (vigencia indefinida)
    true // is_active
);

// Crear tarifa para EPS Sura (con contrato)
ContractId contractSura = ContractId.of("contract-sura-2025");
Money epsRate = Money.of(new BigDecimal("55000"), "COP");

Rate epsRateSura = new Rate(
    epsRate,
    RateId.generate(),
    cleaningService,
    "EPS",
    contractSura, // obligatorio para EPS
    "COP",
    LocalDate.of(2025, 1, 1).atStartOfDay(), // vigencia desde 01/01/2025
    LocalDate.of(2025, 12, 31).atTime(23, 59, 59), // vigencia hasta 31/12/2025
    true
);

// Validar vigencia al facturar
LocalDateTime invoiceDate = LocalDateTime.of(2025, 6, 15, 10, 0);
if (epsRateSura.isValidAt(invoiceDate)) {
    // Usar en factura
    InvoiceItem item = new InvoiceItem(
        InvoiceItemId.generate(),
        cleaningService.getCode(),
        cleaningService.getName(),
        1,
        epsRateSura.getAmount(), // snapshot inmutable
        "COP",
        epsRateSura.getId(),
        invoiceDate,
        providerId,
        invoiceId
    );
} else {
    // Lanza ERR_RATE_EXPIRED
    throw new BusinessRuleViolationException(
        RateError.ERR_RATE_EXPIRED,
        "Rate",
        "Tarifa vencida al momento de facturar"
    );
}

// Ajustar monto con justificación
Money newAmount = Money.of(new BigDecimal("60000"), "COP");
privateRate.adjustAmount(
    newAmount,
    "Ajuste por inflación 2025 (+9%)"
);

// Extender vigencia de tarifa EPS
LocalDateTime newValidTo = LocalDate.of(2026, 12, 31).atTime(23, 59, 59);
epsRateSura.extendValidityTo(newValidTo);

// Desactivar tarifa obsoleta
try {
    privateRate.deactivate("Reemplazada por nueva tarifa 2026");
} catch (BusinessRuleViolationException e) {
    // Lanza ERR_RATE_HAS_ACTIVE_INVOICES si hay facturas pendientes
    System.err.println("No se puede desactivar: " + e.getMessage());
}
```

---

## Métricas de Gestión

**Tarifas Vigentes por Pagador**
```sql
SELECT 
    payer_type,
    COUNT(*) as total_active_rates,
    AVG(amount) as avg_rate
FROM rate
WHERE is_active = true
  AND (valid_to IS NULL OR valid_to >= NOW())
GROUP BY payer_type;
```

**Tarifas Próximas a Vencer (30 días)**
```sql
SELECT 
    r.id,
    s.name as service_name,
    r.payer_type,
    r.valid_to,
    DATEDIFF(r.valid_to, NOW()) as days_until_expiry
FROM rate r
JOIN provided_service s ON s.id = r.service_id
WHERE r.is_active = true
  AND r.valid_to IS NOT NULL
  AND r.valid_to BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY)
ORDER BY days_until_expiry ASC;
```

**Comparación de Tarifas por Pagador**
```sql
SELECT 
    s.name as service_name,
    MAX(CASE WHEN r.payer_type = 'private' THEN r.amount END) as private_rate,
    MAX(CASE WHEN r.payer_type = 'EPS' THEN r.amount END) as eps_rate,
    MAX(CASE WHEN r.payer_type = 'PREPAGADA' THEN r.amount END) as prepaid_rate
FROM provided_service s
JOIN rate r ON r.service_id = s.id
WHERE r.is_active = true
GROUP BY s.id
ORDER BY private_rate DESC;
```

**Histórico de Ajustes de Tarifa**
```sql
SELECT 
    s.name as service_name,
    r.payer_type,
    arc.old_amount,
    arc.new_amount,
    ((arc.new_amount - arc.old_amount) / arc.old_amount * 100) as percentage_change,
    arc.justification,
    arc.adjusted_at
FROM rate r
JOIN provided_service s ON s.id = r.service_id
JOIN audit_rate_change arc ON arc.rate_id = r.id
WHERE arc.adjusted_at >= NOW() - INTERVAL 6 MONTH
ORDER BY arc.adjusted_at DESC;
```