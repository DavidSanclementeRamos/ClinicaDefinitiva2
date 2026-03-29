package com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ReportStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PageAdministrativeReportDto(
        Long id,
        String title,
        PeriodDto period,
        ReportStatus status,
        LocalDateTime createdAt,
        int totalItems
) {}
