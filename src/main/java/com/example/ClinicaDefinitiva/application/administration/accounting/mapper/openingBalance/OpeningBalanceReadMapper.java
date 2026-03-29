package com.example.ClinicaDefinitiva.application.administration.accounting.mapper.openingBalance;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.ReadOpeningBalanceDto;
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
                balance.getFecha()
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