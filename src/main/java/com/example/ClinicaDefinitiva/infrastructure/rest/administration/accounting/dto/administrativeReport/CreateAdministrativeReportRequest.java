
package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateAdministrativeReportRequest(
    @NotBlank String title,
    @NotNull LocalDate periodStart,
    @NotNull LocalDate periodEnd,
    @NotNull Long createdBy
) {}
