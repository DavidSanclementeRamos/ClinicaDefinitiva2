package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.rate;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.enu.PayerType;
import com.example.ClinicaDefinitiva.domain.billing.enu.RateStatus;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.RateEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class RateReadEntityMapper {

    public Rate toDomain(RateEntity entity) {
        if (entity == null) return null;

        return Rate.builder()
                .id(RateId.of(entity.getId()))
                .serviceId(ServiceId.of(entity.getDentalService().getId()))
                .payerType(PayerType.valueOf(entity.getPayerType()))
                .contractId(entity.getContract() != null ?
                        ContractId.of(entity.getContract().getId()) : null)
                .amount(Price.of(entity.getAmount(), Currency.getInstance(entity.getCurrency())))
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidUntil())
                .status(RateStatus.valueOf(entity.getStatus()))
                .build();
    }
}