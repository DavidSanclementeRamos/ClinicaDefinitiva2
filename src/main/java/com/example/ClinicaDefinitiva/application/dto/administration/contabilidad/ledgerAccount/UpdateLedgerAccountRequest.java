package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record UpdateLedgerAccountRequest(
        NameDto name,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
