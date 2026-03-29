package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(
                Long companyId,

    @NotBlank @Pattern(regexp = "^[0-9]{1,8}$") String code,
    @NotBlank String name,
    @NotBlank String nature,
    @NotNull Boolean requiresThirdParty,
    @NotNull Boolean requiresDocument
) {}