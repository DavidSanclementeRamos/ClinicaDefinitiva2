package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import java.math.BigDecimal;

public record IndicatorDto(String name, BigDecimal value, String unit) {
}
