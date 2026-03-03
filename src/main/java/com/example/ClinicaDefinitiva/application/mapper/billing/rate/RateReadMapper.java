package com.example.ClinicaDefinitiva.application.mapper.billing.rate;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import org.springframework.stereotype.Component;

@Component
public class RateReadMapper {

    public ReadRateDto toDto(Rate rate) {
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