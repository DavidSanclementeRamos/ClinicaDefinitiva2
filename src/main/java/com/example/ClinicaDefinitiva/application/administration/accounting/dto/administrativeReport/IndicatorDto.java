package com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport;

import java.math.BigDecimal;

public record IndicatorDto(String name, BigDecimal value, String unit) {
}
