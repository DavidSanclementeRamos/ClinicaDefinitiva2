package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.LedgerAccount;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.CreateLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.UpdateLedgerAccountDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.CreateAccountRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.UpdateAccountRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountRestWriteMapper {

    public CreateLedgerAccountDto toServiceCreate(CreateAccountRequest request) {
        if (request == null) return null;

        return new CreateLedgerAccountDto(
                request.companyId(),
                request.code(),
                request.name(),
                request.nature(),
                request.requiresThirdParty(),
                request.requiresDocument()
        );
    }

    public UpdateLedgerAccountDto toServiceUpdate(UpdateAccountRequest request) {
        if (request == null) return null;

        return new UpdateLedgerAccountDto(
                request.name(),
                request.requiresThirdParty(),
                request.requiresDocument()
        );
    }
}