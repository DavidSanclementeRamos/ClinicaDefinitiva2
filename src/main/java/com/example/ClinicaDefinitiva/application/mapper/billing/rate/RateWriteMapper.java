package com.example.ClinicaDefinitiva.application.mapper.billing.rate;

import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.ServiceId;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class RateWriteMapper {

    public ServiceId toServiceId(CreateRateDto dto) {
        return ServiceId.of(dto.serviceId());
    }

    public PayerType toPayerType(CreateRateDto dto) {
        return PayerType.valueOf(dto.payerType());
    }

    public ContractId toContractId(CreateRateDto dto) {
        return ContractId.of(dto.contractId());
    }

    public Price toAmount(CreateRateDto dto) {
        return Price.of(dto.amount(), Currency.getInstance(dto.currency()));
    }

    
}
