package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.LedgerAccount;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.PageLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.PageAccountResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.ReadAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountRestReadMapper {

    public ReadAccountResponse toRest(ReadLedgerAccountDto dto) {
        if (dto == null) return null;

        return new ReadAccountResponse(
                dto.id(),
                dto.code(),
                dto.name(),
                dto.nature(),
                dto.requiresThirdParty(),
                dto.requiresDocument(),
                dto.active()
        );
    }

    public PageAccountResponse toPageRest(PageLedgerAccountDto dto) {
        if (dto == null) return null;

        return new PageAccountResponse(
                dto.id(),
                dto.code(),
                dto.name(),
                dto.nature(),
                dto.active()
        );
    }
}
