package com.example.ClinicaDefinitiva.application.mapper.billing;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;

public class RateReadMapper {
    public ReadRateDto toDto(Rate rate) {
        if (rate == null) return null;

        return new ReadRateDto(
                rate.getId().getValue(),
                rate.getServiceId().getId(),
                rate.getPayerType().name(),
                rate.getContractId().getValue(),
                rate.getAmount().asBigDecimal(),
                rate.getAmount().getCurrency().getCurrencyCode(),
                rate.getValidFrom(),
                rate.getValidTo(),
                rate.isActive(),
                rate.isCurrentlyValid()
        );
    }

    public PageRateDto toPageDto(Rate rate) {
        if (rate == null) return null;

        return new PageRateDto(
                rate.getId().getValue(),
                rate.getServiceId().getId(),
                rate.getPayerType().name(),
                rate.getAmount().asBigDecimal(),
                rate.getAmount().getCurrency().getCurrencyCode(),
                rate.getValidFrom(),
                rate.getValidTo(),
                rate.isActive()

        );
    }

}
