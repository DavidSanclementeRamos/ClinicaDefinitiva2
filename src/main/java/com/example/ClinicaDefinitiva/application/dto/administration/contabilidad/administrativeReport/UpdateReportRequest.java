package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;


import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.Name;

public record UpdateReportRequest(
        NameDto title,
        String notes
) {}