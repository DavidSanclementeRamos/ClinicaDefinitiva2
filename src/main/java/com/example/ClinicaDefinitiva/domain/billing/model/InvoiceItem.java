package com.example.ClinicaDefinitiva.domain.billing.model;

import com.example.ClinicaDefinitiva.domain.billing.valueObject.InvoiceItemId;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.Quantity;
import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;


import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad: InvoiceItem (Ítem de factura)
 *
 * Representa una línea de factura asociada a un servicio odontológico.
 * Decisiones de diseño:
 * - Las validaciones de código y descripción del servicio se delegan al módulo DentalService.
 *   Aquí solo se valida que no sean nulos.
 * - Se utiliza un Value Object (Quantity) para encapsular reglas de cantidad.
 * - Se aplica el patrón Builder para evitar constructores con demasiados parámetros.
 */
public final class InvoiceItem {

    private final InvoiceItemId id;

    private final ServiceId serviceId;
    private final String serviceCode;
    private final String serviceDescription;

    private final Price unitPrice;
    private final Quantity quantity;

    private final RateId rateId;
    private final LocalDateTime performedAt;

    private InvoiceItem(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "InvoiceItemId no puede ser nulo");
        this.serviceId = Objects.requireNonNull(builder.serviceId, "ServiceId no puede ser nulo");
        this.serviceCode = Objects.requireNonNull(builder.serviceCode, "ServiceCode no puede ser nulo");
        this.serviceDescription = Objects.requireNonNull(builder.serviceDescription, "ServiceDescription no puede ser nulo");
        this.unitPrice = Objects.requireNonNull(builder.unitPrice, "UnitPrice no puede ser nulo");
        this.quantity = Objects.requireNonNull(builder.quantity, "Quantity no puede ser nulo");
        this.rateId = Objects.requireNonNull(builder.rateId, "RateId no puede ser nulo");
        this.performedAt = Objects.requireNonNull(builder.performedAt, "PerformedAt no puede ser nulo");


    }



    public static InvoiceItem fromRateSnapshot(
            InvoiceItemId id,
            ServiceId serviceId,
            String serviceCode,
            String serviceDescription,
            RateId rate,
            Quantity quantity,
            LocalDateTime performedAt) {

        Objects.requireNonNull(rate, "Rate no puede ser nulo");

        return builder()
                .id(id)
                .serviceId(serviceId)
                .serviceCode(serviceCode)
                .serviceDescription(serviceDescription)
                .quantity(quantity)
                .rateId(rate)
                .performedAt(performedAt)
                .build();
    }
    public Price getTotalPrice() {
        return unitPrice.multiply(quantity.getValue());
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private InvoiceItemId id;
        private ServiceId serviceId;
        private String serviceCode;
        private String serviceDescription;
        private Price unitPrice;
        private Quantity quantity;
        private RateId rateId;
        private LocalDateTime performedAt;

        public Builder id(InvoiceItemId id) { this.id = id; return this; }
        public Builder serviceId(ServiceId serviceId) { this.serviceId = serviceId; return this; }
        public Builder serviceCode(String serviceCode) { this.serviceCode = serviceCode; return this; }
        public Builder serviceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; return this; }
        public Builder unitPrice(Price unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder quantity(Quantity quantity) { this.quantity = quantity; return this; }
        public Builder rateId(RateId rateId) { this.rateId = rateId; return this; }
        public Builder performedAt(LocalDateTime performedAt) { this.performedAt = performedAt; return this; }

        public InvoiceItem build() { return new InvoiceItem(this); }
    }


    public InvoiceItemId getId() { return id; }
    public ServiceId getServiceId() { return serviceId; }
    public String getServiceCode() { return serviceCode; }
    public String getServiceDescription() { return serviceDescription; }
    public Price getUnitPrice() { return unitPrice; }
    public Quantity getQuantity() { return quantity; }
    public RateId getRateId() { return rateId; }
    public LocalDateTime getPerformedAt() { return performedAt; }
}
