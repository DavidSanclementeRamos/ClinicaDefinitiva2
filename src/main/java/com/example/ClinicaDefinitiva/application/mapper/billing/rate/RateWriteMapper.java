package com.example.ClinicaDefinitiva.application.mapper.billing;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;

import java.util.Currency;

public class RateWriteMapper {

    public Rate fromCreateDto(CreateRateDto dto) {
        if (dto == null) return null;

        return Rate.builder()
                //.id(RateId.create())
                .serviceId(ServiceId.of(dto.serviceId()))
                .payerType(Rate.PayerType.valueOf(dto.payerType()))
                .contractId(dto.contractId() != null ? ContractId.of(dto.contractId()) : null)
                .amount(Price.of(dto.amount(), Currency.getInstance(dto.currency())))
                .validFrom(dto.validFrom())
                .validTo(dto.validTo())
                .active(true)
                .build();
    }
}
