package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount;

public record PageLedgerAccountDto(
        Long id,
        String code,
        String name,
        String nature,
        boolean active,
        int level
) {}

