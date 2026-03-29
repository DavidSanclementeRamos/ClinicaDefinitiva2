package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PageAdministrativeReportResponse(
    Long id,
    String title,
    LocalDate periodStart,
    LocalDate periodEnd,
    String status,
    LocalDateTime createdAt,
    int totalItemsCount
) {}
