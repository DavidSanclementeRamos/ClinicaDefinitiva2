package com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount;

public record PageLedgerAccountDto(
        Long id,
        String code,
        String name,
        String nature,
        boolean active,
        int level
) {}

