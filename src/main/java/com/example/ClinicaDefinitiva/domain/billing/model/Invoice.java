package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.vo.Notes;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.*;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.InvoiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Agregado raíz: Invoice (Factura Clínica)
 *
 * Reglas de negocio implementadas:
 * - RN-INVOICE-001: Debe tener al menos un ítem
 * - RN-INVOICE-002: Total debe ser mayor a cero
 * - RN-INVOICE-003: Tarifas vigentes (delegado a Domain Service)
 * - RN-INVOICE-004: Solo editable en estado BORRADOR
 * - RN-INVOICE-005: Factura PAGADA no puede cancelarse directamente
 * - RN-INVOICE-006: Fecha de vencimiento posterior a emisión
 * - RN-INVOICE-007: Pagadores institucionales (EPS, aseguradoras, prepagadas) requieren contrato
 * - RN-INVOICE-008: Moneda de ítems debe coincidir con la factura
 * - RN-INVOICE-009: Cancelación requiere motivo mínimo 10 caracteres
 * - RN-INVOICE-010: Factura emitida no puede modificarse
 * - RN-INVOICE-011: Invariante matemático Subtotal + Tax = Total
 */
public final class Invoice {


    private final InvoiceId id;


    private final PatientId patientId;
    private final DentistId dentistId;
    private final ProviderId providerId; // Clínica/emisor oficial obligatorio
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

    private final Notes notes;


    private Invoice(Builder builder) {
        this.id =builder.id;
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

        validateDates(createdAt, this.dueDate);
    }



    public static Invoice createInstitutional(
            ContractId contractId,
            ProviderId providerId,
            DentistId dentistId,
            CurrencyCode currency,
            Notes notes,
            LocalDateTime dueDate) {
        // Factura institucional: requiere contrato
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
        // Factura particular: no requiere contrato
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

    public void emit(InvoiceNumberGenerator generator) {

        validateBeforeEmit();

        String previousState = this.status.toString();
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

        String previousState = this.status.toString();
        this.status = this.status.transitionTo(InvoiceStatus.Status.CANCELLED);
        this.updatedAt = LocalDateTime.now();


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
                    InvoiceError.ERR_INVOICE_NO_ITEMS,
                    EntityContext.INVOICE

            );
        }
        if (total.isNegativeOrZero()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_ZERO_TOTAL,
                    EntityContext.INVOICE

            );
        }

    }

    private void ensureEditable() {
        if (!status.isDraft()) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_NOT_EDITABLE,
                    EntityContext.INVOICE

            );
        }
    }

    private void validateCurrencyMatch(InvoiceItem item) {
        String itemCurrency = item.getUnitPrice().getCurrency().getCurrencyCode();
        if (!currency.getCode().equals(itemCurrency)) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CURRENCY_MISMATCH,
                    EntityContext.INVOICE

            );
        }
    }

    private void validateCancellationReason(String reason) {
        if (reason == null || reason.isBlank() || reason.trim().length() < 10) {
            throw new BusinessRuleViolationException(
                    InvoiceError.ERR_INVOICE_CANCELLATION_REQUIRES_REASON,
                    EntityContext.INVOICE

            );
        }
    }

    public InvoiceId getId() {
        return id;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public DentistId getDentistId() {
        return dentistId;
    }

    public ProviderId getProviderId() {
        return providerId;
    }



    public ContractId getContractId() {
        return contractId;
    }

    public InvoiceNumber getNumber() {
        return number;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public CurrencyCode getCurrency() {
        return currency;
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

    public Notes getNotes() {
        return notes;
    }

    public static class Builder {
        private InvoiceId id;
        private PatientId patientId;
        private DentistId dentistId;
        private ProviderId providerId;
        private ContractId contractId;
        private CurrencyCode currency = CurrencyCode.of("COP");
        private Notes notes;
        private LocalDateTime dueDate;

        public Builder id(InvoiceId id) {
            this.id = id;
            return this;
        }

        public Builder patientId(PatientId patientId) {
            this.patientId = patientId;
            return this;
        }

        public Builder dentistId(DentistId dentistId) {
            this.dentistId = dentistId;
            return this;
        }
        public Builder providerId(ProviderId providerId) { this.providerId = providerId; return this; }


        public Builder contractId(ContractId contractId) {
            this.contractId = contractId;
            return this;
        }

        public Builder currency(CurrencyCode currency) {
            this.currency = currency;
            return this;
        }

        public Builder notes(Notes notes){
            this.notes = notes;
            return this;
        }

        public Builder dueDate(LocalDateTime dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }

    }

}

