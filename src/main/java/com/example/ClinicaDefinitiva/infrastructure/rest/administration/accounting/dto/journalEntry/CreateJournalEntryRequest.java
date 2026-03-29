package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateJournalEntryRequest(
    @NotNull Long companyId,
    @NotNull LocalDate date,
    @NotBlank String documentNumber,
    @NotBlank String description
) {}