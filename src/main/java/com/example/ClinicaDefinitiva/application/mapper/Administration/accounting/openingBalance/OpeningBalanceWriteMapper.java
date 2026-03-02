package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.openingBalance;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;

import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceWriteMapper {

    public CompanyId toCompanyId(CreateOpeningBalanceDto dto) {
        return CompanyId.of(dto.companyId());
    }

    public LedgerAccountId toLedgerAccountId(CreateOpeningBalanceDto dto) {
        return LedgerAccountId.of(dto.accountId());
    }

    public ThirdPartiesId toThirdPartiesId(CreateOpeningBalanceDto dto) {
        return ThirdPartiesId.of(dto.thirdPartiesId());
    }

    public Price toPrice(CreateOpeningBalanceDto dto) {
        return Price.of(dto.amount(), Currency.getInstance(dto.currency()));
    }
}
