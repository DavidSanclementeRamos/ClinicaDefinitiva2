package com.example.ClinicaDefinitiva.domain.billing.model;


import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumberGenerator;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceNumber;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.ProviderId;
import com.example.ClinicaDefinitiva.domain.billing.vo.CurrencyCode;
import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.payment.event.InvoiceFullyPaidEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado raíz: Invoice (Factura Clínica)
 *
 * Reglas de negocio (adiciones respecto a versión anterior):
 * - RN-INVOICE-016: La factura debe estar en PENDING para poder registrarse como pagada.
 * - RN-INVOICE-017: La factura se marca como PAID automáticamente cuando
 *                   los pagos acumulados cubren el total.
 *
 * Coordinación con Payment:
 * - Payment publica PaymentConfirmedEvent al confirmarse.
 * - PaymentConfirmedEventHandler llama a invoice.receivePayment(amount).
 * - invoice.receivePayment() acumula totalPaid y emite InvoiceFullyPaidEvent si corresponde.
 * - El agregado NO accede directamente a Payment: cada uno vive en su propio bounded context.
 */
public final class Invoice {

    private final InvoiceId id;
    private final PatientId patientId;
    private final DentistId dentistId;
    private final ProviderId providerId;
    private final ContractId contractId;

    private InvoiceNumber number;
    private LocalDateTime updatedAt;
    private final LocalDateTime dueDate;
    private InvoiceStatus status;

    private final List<InvoiceItem> items;
    private final CurrencyCode currency;
    private Price subtotal;
    private Price tax;
    private Price total;
    private Price totalPaid;   // ← acumula los pagos confirmados recibidos

    private final Notes notes;

    /**
     * Eventos pendientes de publicación.
     * El Application Service los extrae con pullDomainEvents() tras persistir.
     */
    private final List<Object> pendingEvents = new ArrayList<>();

    private Invoice(Builder builder) {
        this.id = builder.id;
        this.patientId = builder.patientId;
        this.dentistId = builder.dentistId;
        this.providerId = builder.providerId;
        this.contractId = builder.contractId;
        this.currency = builder.currency;
        this.notes = builder.notes;
        this.dueDate = builder.dueDate;
        LocalDateTime createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
        this.status = InvoiceStatus.draft();
        this.items = new ArrayList<>();
        this.subtotal = Price.zero(currency.toJavaCurrency());
        this.tax = Price.zero(currency.toJavaCurrency());
        this.total = Price.zero(currency.toJavaCurrency());
        this.totalPaid = Price.zero(currency.toJavaCurrency());

        validateDates(createdAt, this.dueDate);
    }

        public static Invoice createInstitutional(
            ContractId contractId,
            ProviderId providerId,
            DentistId dentistId,
            CurrencyCode currency,
            Notes notes,
            LocalDateTime dueDate) {
        return new Builder()
                .contractId(contractId)
                .providerId(providerId)
                .dentistId(dentistId)
                .currency(currency)
                .notes(notes)
                .dueDate(dueDate)
                .build();
    }

    public static Invoice createParticular(
            PatientId patientId,
            ProviderId providerId,
            DentistId dentistId,
            CurrencyCode currency,
            Notes notes,
            LocalDateTime dueDate) {
        return new Builder()
                .patientId(patientId)
                .providerId(providerId)
                .dentistId(dentistId)
                .currency(currency)
                .notes(notes)
                .dueDate(dueDate)
                .build();
    }

    
    public void addItem(InvoiceItem item) {
        ensureEditable();
        validateCurrencyMatch(item);
        items.add(item);
        recalcTotals();
    }

    public void emit(InvoiceNumberGenerator generator) {
        validateBeforeEmit();
        this.number = generator.next();
        this.status = this.status.transitionTo(InvoiceStatus.Status.PENDING);
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(String reason) {
        validateCancellationReason(reason);

        if (status.isPaid()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_CANNOT_CANCEL_PAID,
                EntityContext.INVOICE
            );
        }

        this.status = this.status.transitionTo(InvoiceStatus.Status.CANCELLED);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * RN-INVOICE-016 + RN-INVOICE-017
     *
     * Registra un pago parcial o total confirmado sobre esta factura.
     * Sólo puede llamarse si la factura está en estado PENDING.
     *
     * Cuando totalPaid >= total la factura transiciona a PAID automáticamente
     * y publica InvoiceFullyPaidEvent.
     *
     * Este método es invocado por PaymentConfirmedEventHandler; el agregado
     * Invoice no conoce el agregado Payment directamente.
     *
     * @param paymentAmount Monto del pago confirmado (en la misma moneda que la factura)
     */
    public void receivePayment(Price paymentAmount) {
        // RN-INVOICE-016: solo facturas PENDING pueden recibir pagos
        if (!status.isPending()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_MUST_BE_PENDING_TO_PAY,
                EntityContext.INVOICE
            );
        }

        this.totalPaid = this.totalPaid.add(paymentAmount);
        this.updatedAt = LocalDateTime.now();

        // RN-INVOICE-017: si los pagos acumulados cubren el total → PAID
        if (this.totalPaid.isGreaterThanOrEqual(this.total)) {
            this.status = this.status.transitionTo(InvoiceStatus.Status.PAID);
            pendingEvents.add(new InvoiceFullyPaidEvent(id));
        }
    }

    /**
     * Extrae y limpia los eventos pendientes.
     * El Application Service los publica tras persistir el agregado.
     */
    public List<Object> pullDomainEvents() {
        List<Object> events = Collections.unmodifiableList(new ArrayList<>(pendingEvents));
        pendingEvents.clear();
        return events;
    }

    // Queries / estado calculado

    public boolean isFullyPaid() {
        return totalPaid.isGreaterThanOrEqual(total);
    }

    public Price getRemainingBalance() {
        return total.subtract(totalPaid);
    }

    

    private void recalcTotals() {
        Price sum = Price.zero(currency.toJavaCurrency());
        for (InvoiceItem item : items) {
            sum = sum.add(item.getTotalPrice());
        }
        this.subtotal = sum;
        this.tax = computeTax(this.subtotal);
        this.total = this.subtotal.add(this.tax);
        this.updatedAt = LocalDateTime.now();
        validateTotalsInvariant();
    }

    private Price computeTax(Price base) {
        // Delegar a TaxPolicy en producción
        return Price.zero(currency.toJavaCurrency());
    }

    private void validateTotalsInvariant() {
        Price expectedTotal = this.subtotal.add(this.tax);
        if (!this.total.equals(expectedTotal)) {
            throw new IllegalStateException("Invariant broken: Subtotal + Tax ≠ Total");
        }
    }

    private void validateDates(LocalDateTime createdAt, LocalDateTime dueDate) {
        if (dueDate != null && !dueDate.isAfter(createdAt)) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_INVALID_DUE_DATE,
                EntityContext.INVOICE
            );
        }
    }

    private void validateBeforeEmit() {
        if (items.isEmpty()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_NO_ITEMS, EntityContext.INVOICE);
        }
        if (total.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_ZERO_TOTAL, EntityContext.INVOICE);
        }
    }

    private void ensureEditable() {
        if (!status.isDraft()) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_NOT_EDITABLE, EntityContext.INVOICE);
        }
    }

    private void validateCurrencyMatch(InvoiceItem item) {
        String itemCurrency = item.getUnitPrice().getCurrency().getCurrencyCode();
        if (!currency.getCode().equals(itemCurrency)) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_CURRENCY_MISMATCH, EntityContext.INVOICE);
        }
    }

    private void validateCancellationReason(String reason) {
        if (reason == null || reason.isBlank() || reason.trim().length() < 10) {
            throw new BusinessRuleViolationException(
                InvoiceError.ERR_INVOICE_CANCELLATION_REQUIRES_REASON, EntityContext.INVOICE);
        }
    }

    // Getters
    public InvoiceId getId()              { return id; }
    public PatientId getPatientId()       { return patientId; }
    public DentistId getDentistId()       { return dentistId; }
    public ProviderId getProviderId()     { return providerId; }
    public ContractId getContractId()     { return contractId; }
    public InvoiceNumber getNumber()      { return number; }
    public LocalDateTime getUpdatedAt()   { return updatedAt; }
    public LocalDateTime getDueDate()     { return dueDate; }
    public InvoiceStatus getStatus()      { return status; }
    public List<InvoiceItem> getItems()   { return items; }
    public CurrencyCode getCurrency()     { return currency; }
    public Price getSubtotal()            { return subtotal; }
    public Price getTax()                 { return tax; }
    public Price getTotal()               { return total; }
    public Price getTotalPaid()           { return totalPaid; }
    public Notes getNotes()               { return notes; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private InvoiceId id;
        private PatientId patientId;
        private DentistId dentistId;
        private ProviderId providerId;
        private ContractId contractId;
        private CurrencyCode currency = CurrencyCode.of("COP");
        private Notes notes;
        private LocalDateTime dueDate;

        public Builder id(InvoiceId id)               { this.id = id; return this; }
        public Builder patientId(PatientId p)          { this.patientId = p; return this; }
        public Builder dentistId(DentistId d)          { this.dentistId = d; return this; }
        public Builder providerId(ProviderId p)        { this.providerId = p; return this; }
        public Builder contractId(ContractId c)        { this.contractId = c; return this; }
        public Builder currency(CurrencyCode c)        { this.currency = c; return this; }
        public Builder notes(Notes n)                  { this.notes = n; return this; }
        public Builder dueDate(LocalDateTime d)        { this.dueDate = d; return this; }

        public Invoice build() { return new Invoice(this); }
    }
}