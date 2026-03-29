package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount;

public record PageAccountResponse(
    Long id,
    String code,
    String name,
    String nature,
    boolean active
) {}
