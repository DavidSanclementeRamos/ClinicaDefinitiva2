package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.openingBalance;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.vo.Price;

import java.util.Currency;

public class OpeningBalanceWriteMapper {
    public OpeningBalance fromCreateDto(CreateOpeningBalanceDto dto){
        return OpeningBalance.registerOpeningBalance(
                CompanyId.of( dto.companyId()),
                LedgerAccountId.of(  dto.accountId()),
                ThirdPartiesId.of(  dto.thirdPartiesId()),
                Price.of( dto.amount(), Currency.getInstance(dto.currency()))

        );
    }
}
