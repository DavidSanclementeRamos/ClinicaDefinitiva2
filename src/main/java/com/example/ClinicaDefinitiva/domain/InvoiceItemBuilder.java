package com.example.ClinicaDefinitiva.domain;
// InvoiceItemBuilder.java (fluent builder)
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.InvoiceItem;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class InvoiceItemBuilder {
    private String id = UUID.randomUUID().toString();
    private String serviceCode;
    private String description;
    private int quantity = 1;
    private double unitPrice;
    private String rateId;
    private LocalDateTime performedAt;
    private String providerId;
    private Money currency;

    public InvoiceItemBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public InvoiceItemBuilder withService(String serviceCode) {
        this.serviceCode = serviceCode;
        return this;
    }

    public InvoiceItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public InvoiceItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public InvoiceItemBuilder withUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public InvoiceItemBuilder withRateId(String rateId) {
        this.rateId = rateId;
        return this;
    }

    public InvoiceItemBuilder withPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
        return this;
    }

    public InvoiceItemBuilder withProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public InvoiceItem build() {
        return new InvoiceItem(id, serviceCode, description, quantity, unitPrice, rateId, performedAt, providerId);
    }
}
