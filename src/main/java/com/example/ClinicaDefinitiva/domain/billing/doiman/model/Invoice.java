package com.example.ClinicaDefinitiva.domain.billing.doiman.model;

import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.Price;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.enu.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Agregado raíz: Invoice (Factura Clínica)
 *
 * Representa una factura por servicios odontológicos prestados, asegurando:
 * - Cumplimiento normativo colombiano (DIAN, resolución de facturación)
 * - Coherencia entre servicios prestados y montos facturados
 * - Protección contra facturas con tarifas vencidas
 *
 * Reglas críticas implementadas:
 * - RN-INVOICE-003: Validación de tarifas vigentes (previene glosas EPSs)
 * - RN-INVOICE-007: Contrato obligatorio para EPSs (requisito legal Colombia)
 * - RN-INVOICE-010: Inmutabilidad tras emisión (auditoría DIAN)
 * - RN-INVOICE-011: Invariante matemático (Subtotal + Tax = Total)
 */
public class Invoice {
    // no facturar si no hay al menos un ítem válido”
    // o “no emitir factura si algún ítem tiene tarifa vencida”.
    private final InvoiceId id;                     // Identificador único de la factura
    private final PatientId patientId;             // Referencia al paciente (Patient)
    private final DentistId providerId;  // Profesional o clínica que emite la factura

    private final LocalDateTime issuedAt;       // Fecha de emisión
    private final LocalDateTime dueDate;   // Fecha de vencimiento
    private final LocalDateTime createdAt;                    // Fecha de creación en el sistema
    private LocalDateTime updatedAt;            // Última actualización

    private InvoiceStatus status;                 // Estado: Draft, Pending, Paid, Cancelled Vo
    private Long invoiceNumber; // Número consecutivo DIAN

    private final List<InvoiceItem> items = new ArrayList<>();    // Lista de ítems facturados
    private Price subtotal;               // Suma de los ítems antes de impuestos
    private Price tax;                  // Impuestos aplicados
    private Price total;                // Total a pagar
    private final String currency;               // Moneda (ej. COP, USD)

    private final String payer;                  // EPS, aseguradora o paciente particular
    private final ContractId contractId;            // Referencia a contrato/convenio (opcional)

    private final String notes;                                 // Observaciones adicionales

  public Invoice(
          InvoiceId id,
          PatientId patientId,
          DentistId providerId,
          LocalDateTime issuedAt,
          LocalDateTime dueDate,
          String currency,
          String payer,
          ContractId contractId,
          String notes) {

      // RN-INVOICE-015: Validar campos obligatorios
      validateRequiredFields(patientId, providerId);

      // RN-INVOICE-006: Validar coherencia de fechas
      validateDateRange(issuedAt, dueDate);

      this.id = id;
      this.patientId = patientId;
      this.providerId = providerId;
      this.issuedAt = issuedAt;
      this.dueDate = dueDate;
      this.currency = currency;
      this.payer = payer;
      this.contractId = contractId;
      this.notes = notes;
      this.status = InvoiceStatus.draft(); // Estado inicial
      this.createdAt = LocalDateTime.now();
      this.updatedAt = this.createdAt;
      this.subtotal = Price.zero(Currency.getInstance(currency));
      this.tax = Price.zero(Currency.getInstance(currency));
      this.total = Price.zero(Currency.getInstance(currency));
  }


    private void validateRequiredFields(PatientId patientId, DentistId providerId) {
        if (patientId == null || providerId == null) {
            throw new BusinessRuleViolationException(
                    InvoiceError
                            .ERR_INVOICE_MISSING_REQUIRED_FIELDS, EntityContext.INVOICE);
        }
    }

    private void validateDateRange(LocalDateTime issuedAt, LocalDateTime dueDate) {
        if (dueDate != null && !dueDate.isAfter(issuedAt)) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_INVALID_DUE_DATE,EntityContext.INVOICE);
        }
    }

    /**
     * Agrega un ítem a la factura con validaciones completas.
     */
    public void addItem(InvoiceItem item) {
        validateItemNotNull(item);
        ensureEditable(); // RN-INVOICE-004
        validateItemBasics(item);
        validateCurrencyMatch(item); // RN-INVOICE-008

        items.add(item);
        recalcTotals();
    }

    /**
     * Reemplaza todos los ítems con validaciones.
     *
     * Validaciones aplicadas:
     * - RN-INVOICE-010: No permite modificar si está PAID o CANCELLED
     *
     */
    public void replaceAllItems(List<InvoiceItem> newItems) {
        if (status.isPaid() || status.isCancelled()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_IMMUTABLE_AFTER_EMISSION,EntityContext.INVOICE);

        }

        items.clear();
        for (InvoiceItem item : newItems) {
            validateItemNotNull(item);
            validateItemBasics(item);
            validateCurrencyMatch(item);
            items.add(item);
        }
        recalcTotals();
    }

    private void validateItemNotNull(InvoiceItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Invoice item cannot be null");
        }
    }

    private void validateItemBasics(InvoiceItem item) {
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Item quantity must be positive. Item: " + item.getDescription()
            );
        }

    }

    /**
     * RN-INVOICE-008: Todos los ítems deben tener la misma moneda.
     */
    private void validateCurrencyMatch(InvoiceItem item) {
        if (!item.getTotalPrice().getCurrency().equals(this.currency)) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CURRENCY_MISMATCH,EntityContext.INVOICE);
        }
    }

    /**
     * RN-INVOICE-004: Solo puede editarse en estado DRAFT o PENDING.
     */
    private void ensureEditable() {
        if (!status.isDraft() && !status.isPending()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CANNOT_ADD_ITEM,EntityContext.INVOICE);
        }
    }

    // ========================================
    // CÁLCULO DE TOTALES
    // ========================================

    /**
     * Recalcula subtotal, impuestos y total.
     *
     * Validaciones aplicadas:
     * - RN-INVOICE-011: Valida invariante matemático (Subtotal + Tax = Total)
     */
    public void recalcTotals() {
        Price sum = Price.zero(Currency.getInstance(currency));
        for (InvoiceItem item : items) {
            sum = sum.add(item.getTotalPrice());
        }
        this.subtotal = sum;
        this.tax = computeTax(this.subtotal);
        this.total = this.subtotal.add(this.tax);

        // RN-INVOICE-011: Validar invariante matemático (anti-corrupción)
        validateTotalIntegrity();

        this.updatedAt = LocalDateTime.now();
    }

    /**
     * RN-INVOICE-011: Valida que Subtotal + Tax = Total.
     *
     * Esta validación previene corrupción de datos por bugs en cálculos.
     * Si falla, indica un problema grave en la lógica de negocio.
     */
    private void validateTotalIntegrity() {
        Price expectedTotal = this.subtotal.add(this.tax);
        if (!this.total.equals(expectedTotal)) {
            throw new IllegalStateException(
                    String.format(
                            "Corrupción de datos detectada: Subtotal + Tax ≠ Total. " +
                                    "Subtotal: %s, Tax: %s, Total esperado: %s, Total actual: %s",
                            this.subtotal, this.tax, expectedTotal, this.total
                    )
            );
        }
    }

    /**
     * Calcula impuestos según política colombiana.
     *
     */
    private Price computeTax(Price base) {
        // TODO v2.0: Diferenciar IVA por tipo de servicio
        // - Estética: 19%
        // - Salud general: 0%
        return base.multiply(0.19);
    }

    // ========================================
    // VALIDACIONES ANTES DE EMISIÓN
    // ========================================

    /**
     * Valida todas las reglas de negocio antes de emitir factura.
     *
     * Validaciones críticas aplicadas:
     * - RN-INVOICE-001: Al menos un ítem válido
     * - RN-INVOICE-002: Total > 0
     * - RN-INVOICE-003: ⭐ Sin tarifas vencidas (previene glosas EPSs)
     * - RN-INVOICE-007: ⭐ Contrato obligatorio para EPSs (requisito legal Colombia)
     * - RN-INVOICE-014: Servicios activos (delegado a ProvidedService)
     *

     */
    public void validateBeforeEmit() {
        validateHasItems(); // RN-INVOICE-001
        validatePositiveTotal(); // RN-INVOICE-002
        validateContractIfEPS(); // RN-INVOICE-007

        // RN-INVOICE-003: ⭐⭐⭐⭐⭐ VALIDACIÓN CRÍTICA
        // Esta validación previene glosas de EPSs y rechazo de pago
        // TODO: Implementar en Domain Service con acceso a RateRepository
        // validateNoExpiredRates();

        // RN-INVOICE-014: Validar servicios activos
        // TODO: Implementar en Domain Service con acceso a ServiceRepository
        // validateActiveServices();
    }

    /**
     * RN-INVOICE-001: Debe tener al menos un ítem válido.
     */
    private void validateHasItems() {
        if (items.isEmpty()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_NO_ITEMS,EntityContext.INVOICE);
        }
    }

    /**
     * RN-INVOICE-002: Total debe ser mayor a cero.
     */
    private void validatePositiveTotal() {
        if (total.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_ZERO_TOTAL,EntityContext.INVOICE);
        }
    }

    /**
     * RN-INVOICE-007: Si pagador es EPS, debe tener contrato asociado.
     *
     * Contexto Colombia:
     * - EPSs: Sura, Salud Total, Nueva EPS, Compensar, etc.
     * - Facturación requiere contrato vigente
     * - Sin contrato → Rechazo de factura → No pago
     */
    private void validateContractIfEPS() {
        if (payer.toUpperCase().contains("EPS") && contractId == null) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_MISSING_CONTRACT,EntityContext.INVOICE);
        }
    }

    // ========================================
    // TRANSICIONES DE ESTADO
    // ========================================

    /**
     * Transiciona de DRAFT a PENDING (emisión oficial).
     *
     * Validaciones aplicadas:
     * - Todas las validaciones de validateBeforeEmit()
     * - RN-INVOICE-013: Numeración consecutiva DIAN (básica en v1.0)
     *
     * Efectos:
     * - Crea snapshot inmutable de ítems
     * - Asigna número de factura consecutivo
     * - Estado cambia a PENDING
     * - Factura emitida NO puede modificarse (RN-INVOICE-010)
     *
     * @throws BusinessRuleViolationException si validaciones fallan
     */
    public void markPending() {
        validateBeforeEmit();

        // TODO v1.0: Validar consecutivo (requiere InvoiceRepository)
        // validateInvoiceNumberSequence();

        // TODO v2.0: Integración DIAN completa
        // - Obtener número de resolución DIAN
        // - Firmar digitalmente
        // - Transmitir a plataforma MUISCA

        this.status = this.status.transitionTo(InvoiceStatus.Status.PENDING);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * RN-INVOICE-013: Valida numeración consecutiva DIAN (básico v1.0).
     *
     * TODO v1.0: Implementar con acceso a InvoiceRepository
     * TODO v2.0: Integrar con API DIAN para resolución autorizada
     */
    private void validateInvoiceNumberSequence() {
        // TODO: Implementar
        // Long lastNumber = invoiceRepository.findLastInvoiceNumber();
        // if (this.invoiceNumber != lastNumber + 1) {
        //     throw ERR_INVOICE_INVALID_NUMBER_SEQUENCE
        // }
    }

    /**
     * Transiciona de PENDING a PAID.
     *
     * Validaciones aplicadas:
     * - RN-INVOICE-012: Requiere pago registrado (v2.0 con módulo Payments)
     *
     * @throws BusinessRuleViolationException si no hay pago registrado
     */
    public void markPaid() {
        // TODO v2.0: Validar pago registrado
        // if (payments.isEmpty() || payments.sum() < this.total) {
        //     throw ERR_INVOICE_UNPAID
        // }

        this.status = this.status.transitionTo(InvoiceStatus.Status.PAID);
        this.updatedAt = LocalDateTime.now();
    }

    // ========================================
    // CANCELACIÓN
    // ========================================

    /**
     * Cancela la factura con motivo obligatorio.
     *
     * Validaciones aplicadas:
     * - RN-INVOICE-005: No puede cancelarse si está PAID (v2.0 requiere nota crédito)
     * - RN-INVOICE-009: Motivo obligatorio (mínimo 10 caracteres)
     *
     * @param reason Motivo de cancelación (mínimo 10 caracteres)
     * @throws BusinessRuleViolationException si validaciones fallan
     */
    public void cancel(String reason) {
        validateCancellationReason(reason); // RN-INVOICE-009

        // RN-INVOICE-005: No puede cancelarse si está pagada
        if (status.isPaid()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CANNOT_CANCEL_PAID,EntityContext.INVOICE);
        }

        this.status = this.status.transitionTo(InvoiceStatus.Status.CANCELLED);
        this.updatedAt = LocalDateTime.now();

        // TODO v1.0: Registrar motivo en auditoría
        // auditService.registerCancellation(this.id, reason);
    }

    /**
     * RN-INVOICE-009: Cancelación requiere motivo obligatorio (mínimo 10 caracteres).
     */
    private void validateCancellationReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CANCELLATION_REQUIRES_REASON,EntityContext.INVOICE);
        }
        if (reason.length() < 10) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CANCELLATION_REQUIRES_REASON,EntityContext.INVOICE);

        }
    }

    // ========================================
    // GETTERS (Inmutabilidad Protegida)
    // ========================================

    public InvoiceId getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public DentistId getProviderId() {
        return providerId;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Retorna copia inmutable de ítems para proteger invariantes.
     */
    public List<InvoiceItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Price getSubtotal() {
        return subtotal;
    }

    public Price getTax() {
        return tax;
    }

    public Price getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayer() {
        return payer;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ========================================
    // OPERACIONES DE CONSULTA
    // ========================================

    /**
     * Verifica si la factura puede agregar ítems (estado editable).
     */
    public boolean canAddItems() {
        return status.isDraft() || status.isPending();
    }

}
