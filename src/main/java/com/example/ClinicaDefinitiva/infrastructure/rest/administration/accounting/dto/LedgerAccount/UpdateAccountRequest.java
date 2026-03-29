package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAccountRequest(
    @NotBlank String name,
    @NotNull Boolean requiresThirdParty,
    @NotNull Boolean requiresDocument
) {}
