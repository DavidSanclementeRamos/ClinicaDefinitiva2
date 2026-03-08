package com.example.ClinicaDefinitiva.application.dto.administration.accounting.ledgerAccount;

public record CreateLedgerAccountDto(
        Long companyId,
        String code,

        String name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
