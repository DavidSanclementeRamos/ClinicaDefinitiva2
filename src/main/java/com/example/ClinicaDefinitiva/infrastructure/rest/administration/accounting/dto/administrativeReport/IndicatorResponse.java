package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import java.math.BigDecimal;

public record IndicatorResponse(
    String name,
    BigDecimal value,
    String unit
) {}
