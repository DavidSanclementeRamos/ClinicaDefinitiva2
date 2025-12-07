package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record LedgerAccountListResponse(
        String id,
        String code,
        NameDto name,
        String nature,
        boolean active,
        int level
) {}

