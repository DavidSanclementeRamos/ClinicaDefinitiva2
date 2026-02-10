package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record CreateLedgerAccountRequest(
        String companyId,
        String code,

        //NameDto name,
        String nature,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
