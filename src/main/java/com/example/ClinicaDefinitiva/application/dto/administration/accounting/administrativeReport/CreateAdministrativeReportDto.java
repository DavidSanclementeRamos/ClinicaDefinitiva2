package com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport;

public record CreateAdministrativeReportDto(
        String title,
        PeriodDto period,
        Long createdBy
) {}