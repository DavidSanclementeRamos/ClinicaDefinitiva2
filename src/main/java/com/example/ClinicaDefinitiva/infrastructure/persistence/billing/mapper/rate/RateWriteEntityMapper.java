package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.rate;

import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.RateEntity;
import org.springframework.stereotype.Component;

@Component
public class RateWriteEntityMapper {

    public RateEntity toEntity(Rate rate) {
        if (rate == null) return null;

        RateEntity entity = new RateEntity();

      /**  if (rate.getId() != null && rate.getId().getValue() != null) {
            entity.setId(rate.getId().getValue());
        }*/

        entity.setPayerType(rate.getPayerType().name());
        entity.setAmount(rate.getAmount().asBigDecimal());
        entity.setCurrency(rate.getAmount().getCurrency().getCurrencyCode());
        entity.setValidFrom(rate.getValidFrom());
        entity.setValidUntil(rate.getValidTo());
        entity.setStatus(rate.getStatus().name());

        // Nota: Las relaciones con DentalServiceEntity y ContractEntity
        // se establecen en el adapter usando los repositorios

        return entity;
    }
}
