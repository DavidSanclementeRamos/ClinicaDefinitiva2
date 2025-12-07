package com.example.ClinicaDefinitiva.application.mapper.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceResponse;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.ThirdParties;
import org.springframework.stereotype.Component;

/**
 * Mapper: OpeningBalance (Domain → DTO)
 */
@Component
public class OpeningBalanceMapper {

    public OpeningBalanceResponse toResponse(OpeningBalance balance,
                                             LedgerAccount account,
                                             ThirdParties thirdParties) {
        return new OpeningBalanceResponse(
                balance.getOpeningBalanceId() != null ? balance.getOpeningBalanceId().getValue() : null,
                balance.getCompanyId() != null ? balance.getCompanyId().getValue() : null,
                balance.getCuentaId() != null ? balance.getCuentaId().getValue() : null,
                account != null ? account.getCode() : null,
                account != null ? account.getName().getName() : null,
                balance.getThirdPartiesId() != null ? balance.getThirdPartiesId().getValue() : null,
                thirdParties != null ? thirdParties.getName().getName() : null,
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency(),
                balance.getFecha().toString()
        );
    }

    public OpeningBalanceListResponse toListResponse(OpeningBalance balance, LedgerAccount account) {
        return new OpeningBalanceListResponse(
                balance.getOpeningBalanceId() != null ? balance.getOpeningBalanceId().getValue() : null,
                account != null ? account.getCode() : null,
                account != null ? account.getName().getName() : null,
                balance.getValor().asBigDecimal(),
                balance.getValor().getCurrency()
        );
    }
}
