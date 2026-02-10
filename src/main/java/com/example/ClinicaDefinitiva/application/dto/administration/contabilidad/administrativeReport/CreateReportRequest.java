package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

public record CreateReportRequest(
        //NameDto title,
        int year,
        int month,
        PeriodDto periodType,
        String createdBy
) {}