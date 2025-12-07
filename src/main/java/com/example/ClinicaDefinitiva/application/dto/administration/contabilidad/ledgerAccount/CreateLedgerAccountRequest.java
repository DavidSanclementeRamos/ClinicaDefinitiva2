package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record CreateLedgerAccountRequest(
        String companyId,
        String code,
        NameDto name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
