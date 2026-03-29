package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import jakarta.validation.constraints.NotBlank;

public record UpdateJournalEntryRequest(
    @NotBlank String description,
    @NotBlank String documentNumber
) {}
