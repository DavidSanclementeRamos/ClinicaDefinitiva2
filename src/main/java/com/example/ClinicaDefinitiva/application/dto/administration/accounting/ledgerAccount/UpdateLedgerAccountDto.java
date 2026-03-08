package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record UpdateLedgerAccountDto(
        String name,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
