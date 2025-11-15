package com.example.ClinicaDefinitiva.application.dto.billing;

import java.math.BigDecimal;

public class InvoiceItemDto {
    public String id;
    public String serviceCode;
    public String description;
    public int quantity;
    public BigDecimal unitPrice;
    public String currency;
    public String rateId;
    public BigDecimal totalPrice;

    public InvoiceItemDto() {
    }

    public InvoiceItemDto(String currency, String description, String id, int quantity, String rateId, String serviceCode, BigDecimal unitPrice) {
        this.currency = currency;
        this.description = description;
        this.id = id;
        this.quantity = quantity;
        this.rateId = rateId;
        this.serviceCode = serviceCode;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getRateId() {
        return rateId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }
}
