package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.openingBalance;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceReadMapper {

    public ReadOpeningBalanceDto toReadDto(OpeningBalance balance) {
        return new ReadOpeningBalanceDto(
                balance.getOpeningBalanceId().getValue(),
                balance.getCompanyId().value(),
                balance.getCuentaId().getValue(),
                balance.getThirdPartiesId().getValue(),
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency().toString(),
                balance.getFecha().toString()
        );
    }

    public PageOpeningBalanceDto toPageDto(OpeningBalance balance) {
        return new PageOpeningBalanceDto(
                balance.getOpeningBalanceId().getValue(),
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency().toString()
        );
    }
}