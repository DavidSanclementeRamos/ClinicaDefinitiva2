package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.Name;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateReportRequest(
        NameDto title,
        int year,
        int month,
        PeriodDto periodType,
        String createdBy
) {}