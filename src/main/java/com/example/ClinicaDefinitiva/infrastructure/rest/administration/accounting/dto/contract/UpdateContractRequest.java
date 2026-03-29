package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract;

import jakarta.validation.constraints.NotBlank;

public record UpdateContractRequest(
    @NotBlank String name,
    String description,
    String origin,
    @NotBlank String coverageType
) {}
