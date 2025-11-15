package com.example.ClinicaDefinitiva.application.dto;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceId;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServiceRenderedDto {
    public ServiceId serviceId;
    public String serviceCode;
    public String description;
    public int quantity;
    public LocalDateTime performedAt;
    public String providerId;
    public ServiceRenderedDto() {}

    public ServiceRenderedDto(ServiceId serviceId, String serviceCode, String description, int quantity, LocalDateTime performedAt, String providerId) {
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId is required");
        this.serviceCode = serviceCode;
        this.description = description;
        this.quantity = quantity;
        this.performedAt = performedAt;
        this.providerId = providerId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPerformedAt(LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
