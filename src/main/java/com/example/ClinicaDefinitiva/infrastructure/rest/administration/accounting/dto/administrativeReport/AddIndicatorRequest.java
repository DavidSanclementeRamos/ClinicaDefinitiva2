package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AddIndicatorRequest(
    @NotBlank String name,
    @NotNull @Positive BigDecimal value,
    @NotBlank String unit
) {}
