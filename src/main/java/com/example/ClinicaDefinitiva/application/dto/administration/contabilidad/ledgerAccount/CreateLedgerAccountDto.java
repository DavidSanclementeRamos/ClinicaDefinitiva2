package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record CreateLedgerAccountDto(
        Long companyId,
        String code,

        String name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
