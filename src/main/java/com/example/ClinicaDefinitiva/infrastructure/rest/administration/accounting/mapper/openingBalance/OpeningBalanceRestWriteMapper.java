package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.openingBalance;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.CreateOpeningBalanceRequest;
import org.springframework.stereotype.Component;

@Component
public class OpeningBalanceRestWriteMapper {

    public CreateOpeningBalanceDto toServiceCreate(CreateOpeningBalanceRequest request) {
        if (request == null) return null;

        return new CreateOpeningBalanceDto(
                request.companyId(),
                request.accountId(),
                request.thirdPartyId(),
                request.amount(),
                request.currency()
        );
    }
}
