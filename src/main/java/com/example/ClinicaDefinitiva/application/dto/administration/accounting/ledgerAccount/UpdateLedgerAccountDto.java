package com.example.ClinicaDefinitiva.application.dto.administration.accounting.ledgerAccount;

public record UpdateLedgerAccountDto(
        String name,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
