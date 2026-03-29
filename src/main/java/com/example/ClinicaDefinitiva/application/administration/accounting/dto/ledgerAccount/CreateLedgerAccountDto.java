package com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount;

public record CreateLedgerAccountDto(
        Long companyId,
        String code,

        String name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
