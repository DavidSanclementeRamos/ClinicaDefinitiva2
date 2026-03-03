package com.example.ClinicaDefinitiva.domain.billing.model;


import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.RateStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorBilling.RateError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado: Rate (Tarifa de Servicios Odontológicos)
 *
 * Representa la tarifa aplicable a un servicio odontológico según:
 * - Tipo de pagador (EPS, particular, aseguradora, ARL, SOAT, medicina prepagada)
 * - Vigencia contractual (validFrom, validTo)
 * - Contrato asociado (obligatorio para EPS en Colombia)
 *
 * Reglas de negocio implementadas:
 * - RN-RATE-001: La tarifa debe estar activa para poder usarse.
 * - RN-RATE-002: La tarifa debe ser válida en la fecha de facturación.
 * - RN-RATE-003: No pueden existir vigencias solapadas para el mismo servicio + pagador.
 * - RN-RATE-004: El contrato es obligatorio para tarifas EPS.
 *
 * Decisiones de diseño:
 * - El periodo de vigencia puede ser indefinido (validTo = null).
 * - Una tarifa usada en facturación no se elimina, solo se desactiva.
 * - Los ajustes de monto generan nuevas tarifas, preservando historial.
 */
public final class Rate {

    private final RateId id;
    private final ServiceId serviceId;
    private final PayerType payerType;
    private final ContractId contractId;
    private final Price amount;
    private final LocalDateTime validFrom;
    private LocalDateTime validTo;

    private RateStatus status;

    private Rate(Builder builder) {
        this.id = builder.id;
        this.serviceId = builder.serviceId;
        this.amount = builder.amount;
        this.payerType = builder.payerType;
        this.validFrom = builder.validFrom;
        this.validTo = builder.validTo;
        this.contractId = builder.contractId;
        this.status =  RateStatus.ACTIVE;

        validateBusinessRules();
    }

    public static Rate create(ServiceId serviceId,
                              Price amount,
                              PayerType payerType,
                              ContractId contractId) {
        return builder()
                .serviceId(serviceId)
                .amount(amount)
                .payerType(payerType)
                .contractId(contractId)
                .validFrom(LocalDateTime.now())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validateBusinessRules() {
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_INVALID_VALIDITY_RANGE,
                    EntityContext.RATE
            );
        }
    }

    /** Finaliza la vigencia de la tarifa. */
    public void endValidityAt(LocalDateTime endDate) {
        if (endDate.isBefore(validFrom)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_INVALID_VALIDITY_RANGE,
                    EntityContext.RATE
            );
        }
        this.validTo = endDate;
        this.status = RateStatus.EXPIRED;
    }

    /** Marca la tarifa como reemplazada por otra. */
    public void markAsReplaced() {
        this.status = RateStatus.REPLACED;
    }

    /** Desactiva la tarifa manualmente (ej. baja administrativa). */
    public void deactivate() {
        this.status = RateStatus.INACTIVE;
    }

    /** Verifica si la tarifa es válida en una fecha específica (RN-RATE-002). */
    public boolean isValidAt(LocalDateTime when) {
        if (status != RateStatus.ACTIVE) return false;
        if (when.isBefore(validFrom)) return false;
        if (validTo != null && when.isAfter(validTo)) return false;
        return true;
    }

    public void ensureValidAt(LocalDateTime when) {
        if (!isValidAt(when)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_NOT_VALID_AT_DATE,
                    EntityContext.RATE
            );
        }
    }

    // Consultas semánticas
    public boolean isActive() { return status == RateStatus.ACTIVE; }
    public boolean isExpired() { return status == RateStatus.EXPIRED; }
    public boolean isReplaced() { return status == RateStatus.REPLACED; }
    public boolean isInactive() { return status == RateStatus.INACTIVE; }
    public boolean isCurrentlyValid() { return isValidAt(LocalDateTime.now()); }
    public boolean isIndefinite() { return validTo == null; }
    public boolean isForEPS() { return payerType == PayerType.EPS; }

    // Getters
    public RateId getId() { return id; }
    public ServiceId getServiceId() { return serviceId; }
    public PayerType getPayerType() { return payerType; }
    public ContractId getContractId() { return contractId; }
    public Price getAmount() { return amount; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public RateStatus getStatus() { return status; }

    public static class Builder {
        private RateId id;
        private ServiceId serviceId;
        private Price amount;
        private PayerType payerType;
        private ContractId contractId;
        private LocalDateTime validFrom;
        private LocalDateTime validTo;
        private RateStatus status = RateStatus.ACTIVE;

        public Builder id(RateId id) { this.id = id; return this; }
        public Builder serviceId(ServiceId serviceId) { this.serviceId = serviceId; return this; }
        public Builder amount(Price amount) { this.amount = amount; return this; }
        public Builder payerType(PayerType payerType) { this.payerType = payerType; return this; }
        public Builder contractId(ContractId contractId) { this.contractId = contractId; return this; }
        public Builder validFrom(LocalDateTime validFrom) { this.validFrom = validFrom; return this; }
        public Builder validTo(LocalDateTime validTo) { this.validTo = validTo; return this; }
        public Builder status(RateStatus status) { this.status = status; return this; }

        public Rate build() { return new Rate(this); }
    }

   

    
}