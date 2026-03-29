package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.openingBalance;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.OpeningBalanceEntity;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceWriteEntityMapper {

    public OpeningBalanceEntity toEntity(OpeningBalance balance) {
        if (balance == null) return null;

        OpeningBalanceEntity entity = new OpeningBalanceEntity();

        /**if (balance.getOpeningBalanceId() != null && balance.getOpeningBalanceId().getValue() != null) {
            entity.setId(balance.getOpeningBalanceId().getValue());
        }*/

        entity.setAmount(balance.getValor().asBigDecimal());
        entity.setCurrency(balance.getValor().getCurrency().getCurrencyCode());
        entity.setDate(balance.getFecha());

        return entity;
    }
}
