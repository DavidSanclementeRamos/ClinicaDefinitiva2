package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport;

import jakarta.validation.constraints.NotNull;

public record AddJournalEntryReferenceRequest(
    @NotNull Long journalEntryId
) {}
