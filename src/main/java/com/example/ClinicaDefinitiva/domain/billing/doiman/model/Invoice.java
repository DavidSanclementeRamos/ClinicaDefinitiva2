package com.example.ClinicaDefinitiva.domain.billing.doiman.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.enu.InvoiceStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Invoice {
    // no facturar si no hay al menos un ítem válido”
    // o “no emitir factura si algún ítem tiene tarifa vencida”.
    private final InvoiceId id;                     // Identificador único de la factura
    private final PatientId patientId;             // Referencia al paciente (Patient)
    private final DentistId providerId;            // Profesional o clínica que emite la factura
    private final LocalDateTime issuedAt;       // Fecha de emisión
    private final LocalDateTime dueDate;          // Fecha de vencimiento
    private InvoiceStatus status;                 // Estado: Draft, Pending, Paid, Cancelled Vo
    private final List<InvoiceItem> items = new ArrayList<>();    // Lista de ítems facturados
    private Price subtotal;               // Suma de los ítems antes de impuestos
    private Price tax;                  // Impuestos aplicados
    private Price total;                // Total a pagar
    private final String currency;               // Moneda (ej. COP, USD)
    private final String payer;                  // EPS, aseguradora o paciente particular
    private final ContractId contractId;            // Referencia a contrato/convenio (opcional)
    private final String notes;                                 // Observaciones adicionales
    private final LocalDateTime createdAt;                    // Fecha de creación en el sistema
    private LocalDateTime updatedAt;            // Última actualización

    public Invoice(InvoiceId id,
                    PatientId patientId,
                    DentistId providerId,
                    LocalDateTime issuedAt,
                    LocalDateTime dueDate,
                    String currency,
                    String payer,
                    ContractId contractId,
                    String notes) {
       // Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(patientId, "patientId is required");
        Objects.requireNonNull(providerId, "providerId is required");
        Objects.requireNonNull(issuedAt, "issuedAt is required");
        this.id = id;
        this.patientId = patientId;
        this.providerId = providerId;
        this.issuedAt = issuedAt;
        this.dueDate = dueDate;
        this.currency = currency;
        this.payer = payer;
        this.contractId = contractId;
        this.notes = notes;
        this.status = InvoiceStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.subtotal = Price.zero(currency);
        this.tax = Price.zero(currency);
        this.total = Price.zero(currency);
    }

    // Add item with domain-level invariants
    public void addItem(InvoiceItem item) {
        if (item == null) throw new IllegalArgumentException("Invoice item cannot be null");
        if (status != InvoiceStatus.DRAFT && status != InvoiceStatus.PENDING) {
            throw new IllegalArgumentException("Cannot add item when invoice status is " + status);
        }
        // Basic item consistency: unitPrice matches computed total / quantity
        if (item.getQuantity() <= 0) throw new IllegalArgumentException("Item quantity must be positive");
       // if (item.getUnitPrice()<= 0) throw new IllegalArgumentException("Item unit price must be positive");
        if (item.getUnitPrice().isNegativeOrZero()) throw new IllegalArgumentException("Item unit price must be positive");

        items.add(item);
        recalcTotals();
    }

    public void replaceAllItems(List<InvoiceItem> newItems) {
        if (status == InvoiceStatus.PAID || status == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot modify items in status " + status);
        }
        items.clear();
        items.addAll(newItems);
        recalcTotals();
    }

    // Recalcula subtotal, impuestos y total
    public void recalcTotals() {
        Price sum = Price.zero(currency);
        for (InvoiceItem it : items) sum = sum.add(it.getTotalPrice());
        this.subtotal = sum;
        this.tax = computeTax(this.subtotal);
        this.total = this.subtotal.add(this.tax);
        this.updatedAt = LocalDateTime.now();

    }

    // Implemente la política de impuestos aquí (simplificada)
    private Price computeTax(Price base) {
        // ejemplo simple 19%
        return base.multiply(0.19);

    }

    // Validaciones antes de emitir
    public void validateBeforeEmit() {
        if (items.isEmpty()) throw new IllegalArgumentException("Invoice must have at least one item");
        if (total.isNegativeOrZero()) throw new IllegalArgumentException("Invoice total must be positive");
        if (status == InvoiceStatus.PAID || status == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot emit invoice in status " + status);
        }
        // otras reglas de dominio específicas pueden agregarse aquí
    }

    // Estado transition example
    public void markPending() {
        validateBeforeEmit();
        this.status = InvoiceStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public DentistId getProviderId() {
        return providerId;
    }
    public String getPayer() {
        return payer;
    }
    public PatientId getPatientId() {
        return patientId;
    }
    public String getNotes() {
        return notes;
    }
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    public String getCurrency() {return currency;}
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public ContractId getContractId() {
        return contractId;
    }
    public List<InvoiceItem> getItems() { return Collections.unmodifiableList(items); }
    public Price getSubtotal() { return subtotal; }
    public Price getTax() { return tax; }
    public Price getTotal() { return total; }
    public InvoiceStatus getStatus() { return status; }
    public InvoiceId getId() { return id; }

   // private static double round(double v) {
      //  return Math.round(v * 100.0) / 100.0;
   // }
}
