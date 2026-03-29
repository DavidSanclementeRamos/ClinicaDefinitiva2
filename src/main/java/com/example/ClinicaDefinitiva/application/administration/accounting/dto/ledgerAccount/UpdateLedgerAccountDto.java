package com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount;

public record UpdateLedgerAccountDto(
        String name,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
