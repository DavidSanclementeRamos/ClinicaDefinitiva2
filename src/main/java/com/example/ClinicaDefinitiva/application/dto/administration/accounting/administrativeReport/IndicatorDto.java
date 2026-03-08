package com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport;

import java.math.BigDecimal;

public record IndicatorDto(String name, BigDecimal value, String unit) {
}
