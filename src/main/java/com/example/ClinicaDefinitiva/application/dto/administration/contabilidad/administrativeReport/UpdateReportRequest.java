package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;


import com.example.ClinicaDefinitiva.application.dto.NameDto;

public record UpdateReportRequest(
        NameDto title,
        String notes
) {}