package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

public record CreateAdministrativeReportDto(
        String title,
        PeriodDto period,
        Long createdBy
) {}