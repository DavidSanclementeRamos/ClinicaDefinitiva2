package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Future;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateContractRequest(
    @NotNull Long companyId,
    @NotNull Long thirdPartyId,
    @NotBlank String name,
    String description,
    String origin,
    @NotNull @Future LocalDate endDate,
    @NotBlank String coverageType,
    @NotNull @Positive BigDecimal coverageRate
) {}
