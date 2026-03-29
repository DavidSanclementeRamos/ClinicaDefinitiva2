package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.openingBalance;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.PageOpeningBalanceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.ReadOpeningBalanceResponse;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceRestReadMapper {

    public ReadOpeningBalanceResponse toRest(ReadOpeningBalanceDto dto) {
        if (dto == null) return null;

        return new ReadOpeningBalanceResponse(
                dto.id(),
                dto.companyId(),
                dto.accountId(),
                dto.thirdPartiesId(),
                dto.amount(),
                dto.currency(),
                dto.date()
        );
    }

    public PageOpeningBalanceResponse toPageRest(PageOpeningBalanceDto dto) {
        if (dto == null) return null;

        return new PageOpeningBalanceResponse(
                dto.id(),
                dto.amount(),
                dto.currency()
        );
    }
}