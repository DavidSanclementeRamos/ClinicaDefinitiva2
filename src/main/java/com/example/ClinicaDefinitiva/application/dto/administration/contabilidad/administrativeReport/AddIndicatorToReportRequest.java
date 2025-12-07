package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

import java.math.BigDecimal;

public record AddIndicatorToReportRequest(
        NameDto name,
        BigDecimal value,
        String unit,
        String description,
        String type
) {}