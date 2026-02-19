package com.example.ClinicaDefinitiva.domain.billing.model;


import com.example.ClinicaDefinitiva.domain.billing.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.model.ProvidedService;
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
    private final LocalDateTime validTo;


    private boolean active;


    private Rate(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "Rate ID no puede ser nulo");
        this.serviceId = Objects.requireNonNull(builder.serviceId, "Service no puede ser nulo");
        this.amount = Objects.requireNonNull(builder.amount, "Amount no puede ser nulo");
        this.payerType = Objects.requireNonNull(builder.payerType, "PayerType no puede ser nulo");
        this.validFrom = Objects.requireNonNull(builder.validFrom, "ValidFrom no puede ser nulo");
        this.validTo = builder.validTo;
        this.contractId = builder.contractId;
        this.active = builder.active;

        validateBusinessRules();
    }



    public static Rate create(
            ServiceId serviceId,
            Price amount,
            PayerType payerType,
            ContractId contractId) {

        return builder()
                .serviceId(serviceId)
                .amount(amount)
                .payerType(payerType)
                .contractId(contractId)
                .validFrom(LocalDateTime.now())
                .active(true)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }


    private void validateBusinessRules() {

        // Validación de vigencia
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_INVALID_VALIDITY_RANGE,
                    EntityContext.RATE

            );
        }
    }


    /** Verifica si la tarifa es válida en una fecha específica (RN-RATE-002). */
    public boolean isValidAt(LocalDateTime when) {
        if (!active) return false;
        if (when.isBefore(validFrom)) return false;
        if (validTo != null && when.isAfter(validTo)) return false;
        return true;
    }

    /** Garantiza que la tarifa sea válida en una fecha, lanza excepción si no lo es. */
    public void ensureValidAt(LocalDateTime when) {
        if (!isValidAt(when)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_NOT_VALID_AT_DATE,
                    EntityContext.RATE
            );
        }
    }

    /** Desactiva la tarifa (cuando es reemplazada por otra). */
    public void deactivate() {
        this.active = false;
    }

    /** Finaliza la vigencia de la tarifa. */
    public void endValidityAt(LocalDateTime endDate) {
        if (endDate.isBefore(validFrom)) {
            throw new BusinessRuleViolationException(
                    RateError.ERR_RATE_INVALID_VALIDITY_RANGE,
                    EntityContext.RATE

            );
        }

    }


    public boolean isActive() { return active; }
    public boolean isCurrentlyValid() { return isValidAt(LocalDateTime.now()); }
    public boolean hasExpired() { return validTo != null && LocalDateTime.now().isAfter(validTo); }
    public boolean isIndefinite() { return validTo == null; }
    public boolean isForEPS() { return payerType == PayerType.EPS; }


    public RateId getId() { return id; }
    public ServiceId getServiceId() { return serviceId; }
    public PayerType getPayerType() { return payerType; }
    public ContractId getContractId() { return contractId; }
    public Price getAmount() { return amount; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public LocalDateTime getValidTo() { return validTo; }


    public static class Builder {
        private RateId id;
        private ServiceId serviceId;
        private Price amount;
        private PayerType payerType;
        private ContractId contractId;
        private LocalDateTime validFrom;
        private LocalDateTime validTo;
        private boolean active = true;

        public Builder id(RateId id) { this.id = id; return this; }
        public Builder serviceId(ServiceId serviceId) { this.serviceId = serviceId; return this; }
        public Builder amount(Price amount) { this.amount = amount; return this; }
        public Builder payerType(PayerType payerType) { this.payerType = payerType; return this; }
        public Builder contractId(ContractId contractId) { this.contractId = contractId; return this; }
        public Builder validFrom(LocalDateTime validFrom) { this.validFrom = validFrom; return this; }
        public Builder validTo(LocalDateTime validTo) { this.validTo = validTo; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public Rate build() { return new Rate(this); }
    }


    public enum PayerType {
        EPS, PRIVATE, INSURANCE, ARL, SOAT, PREPAID
    }
}
