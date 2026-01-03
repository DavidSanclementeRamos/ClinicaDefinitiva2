package com.example.ClinicaDefinitiva.domain.billing.doiman.model;

import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class InvoiceItem {
    // Representa un servicio facturado.

    private final InvoiceItemId id;                   // Identificador único
    private final InvoiceId invoiceId;           // Referencia a la factura
    private final String serviceCode;           // Referencia al servicio prestado
    private final String description;          // Descripción del ítem (ej. "Extracción cordal")
    private final int quantity;             // Cantidad (ej. 1, 2 unidades)
    private final Price unitPrice;         // Precio unitario aplicado
    private final Price totalPrice;        // Total (quantity * unit_price)
    private final RateId rateId;              //Referencia a la tarifa usada
    private final LocalDateTime performedAt;    // Fecha/hora de prestación
    private final DentistId providerId;            // Profesional que realizó el servicio
    private final  String currency;

    public InvoiceItem(InvoiceItemId id,
                String serviceCode,
                String description,
                int quantity,
                Price unitPrice,
                String currency,
                RateId rateId,
                LocalDateTime performedAt,
                DentistId providerId,
                       InvoiceId invoiceId) {
        // Validaciones esenciales
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(serviceCode, "serviceCode is required");
        Objects.requireNonNull(invoiceId, "serviceCode is required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        if (unitPrice.isNegativeOrZero()) throw new IllegalArgumentException("Item unit price must be positive");
        Objects.requireNonNull(performedAt, "performedAt is required");

        this.id = id;
        this.serviceCode = serviceCode;
        this.description = description == null ? "" : description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.rateId = rateId;
        this.performedAt = performedAt;
        this.providerId = providerId;
        this.invoiceId = invoiceId;
        this.currency = currency;
    }

    // Getters (no setters)
    public InvoiceItemId getId() { return id; }
    public String getServiceCode() { return serviceCode; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public Price getUnitPrice() { return unitPrice; }
    public RateId getRateId() { return rateId; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public DentistId getProviderId() { return providerId; }
    public String getCurrency() {return currency;}
    public Price getTotalPrice() {
        return totalPrice;
    }
    public InvoiceId getInvoiceId() {
        return invoiceId;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "id=" + id +
                ", serviceCode='" + serviceCode + '\'' +
                ", qty=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
