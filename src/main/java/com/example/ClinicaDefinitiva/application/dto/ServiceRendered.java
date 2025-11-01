package com.example.ClinicaDefinitiva.application.dto;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceId;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServiceRendered {
    private final ServiceId serviceId;      // referencia al catálogo (ProvidedService.id)
    private final String serviceCode;       // redundancia útil para auditoría (ProvidedService.code)
    private final String description;       // descripción contextual (puede venir del providedService o del registro)
    private final int quantity;             // unidades prestadas
    private final LocalDateTime performedAt;// cuándo se realizó
    private final DentistId providerId;     // quien lo realizó (VO)

    public ServiceRendered(ServiceId serviceId,
                           String serviceCode,
                           String description,
                           int quantity,
                           LocalDateTime performedAt,
                           DentistId providerId) {
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId is required");
        this.serviceCode = Objects.requireNonNull(serviceCode, "serviceCode is required");
        this.description = description == null ? "" : description;
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
        this.performedAt = Objects.requireNonNull(performedAt, "performedAt is required");
        this.providerId = Objects.requireNonNull(providerId, "providerId is required");
    }


    public ServiceId getServiceId() {
        return serviceId;
    }

    public String getServiceCode() { return serviceCode; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public DentistId getProviderId() { return providerId; }




}
