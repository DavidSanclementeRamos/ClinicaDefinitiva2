package com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport;

public record CreateAdministrativeReportDto(
        String title,
        PeriodDto period,
        Long createdBy
) {}