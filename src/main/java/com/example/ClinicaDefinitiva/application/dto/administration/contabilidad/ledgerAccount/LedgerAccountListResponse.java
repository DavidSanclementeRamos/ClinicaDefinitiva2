package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record LedgerAccountListResponse(
        String id,
        String code,
        NameDto name,
        String nature,
        boolean active,
        int level
) {}

