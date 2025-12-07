package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record UpdateLedgerAccountRequest(
        NameDto name,
        boolean requiresThirdParty,
        boolean requiresDocument
) {}
