package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import jakarta.validation.constraints.NotBlank;

public record UpdateAdministrativeReportRequest(
    @NotBlank String title,
    String notes
) {}
