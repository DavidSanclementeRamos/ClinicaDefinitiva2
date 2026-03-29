package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.openingBalance;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.OpeningBalanceEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class OpeningBalanceReadEntityMapper {

    public OpeningBalance toDomain(OpeningBalanceEntity entity) {
        if (entity == null) return null;

        return OpeningBalance.registerOpeningBalance(
                CompanyId.of(entity.getCompany().getId()),
                LedgerAccountId.of(entity.getAccount().getId()),
                entity.getThirdParty() != null ? 
                        ThirdPartiesId.of(entity.getThirdParty().getId()) : null,
                Price.of(entity.getAmount(), Currency.getInstance(entity.getCurrency()))
        );
    }
}