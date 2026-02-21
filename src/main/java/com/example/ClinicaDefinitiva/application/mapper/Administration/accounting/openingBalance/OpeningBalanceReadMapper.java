package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.openingBalance;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceReadMapper {
    public ReadOpeningBalanceDto toReadDto(OpeningBalance balance) {
        return new ReadOpeningBalanceDto(
                balance.getOpeningBalanceId() != null ? balance.getOpeningBalanceId().getValue() : null,
                balance.getCompanyId() != null ? balance.getCompanyId().getValue() : null,
                balance.getCuentaId() != null ? balance.getCuentaId().getValue() : null,
                balance.getThirdPartiesId() != null ? balance.getThirdPartiesId().getValue() : null,
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency().toString(),
                balance.getFecha().toString()
        );
    }

    public PageOpeningBalanceDto toPageDto(OpeningBalance balance, LedgerAccount account) {
        return new PageOpeningBalanceDto(
                balance.getOpeningBalanceId() != null ? balance.getOpeningBalanceId().getValue() : null,
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency().toString()
        );
    }
}
