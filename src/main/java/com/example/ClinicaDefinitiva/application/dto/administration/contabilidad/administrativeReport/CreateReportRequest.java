package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record CreateReportRequest(
        NameDto title,
        int year,
        int month,
        PeriodDto periodType,
        String createdBy
) {}