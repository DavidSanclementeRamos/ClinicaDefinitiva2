package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AddJournalEntryLineRequest(
    @NotNull Long accountId,
    Long thirdPartyId,
    @NotBlank String description,
    @NotNull @Positive BigDecimal amount,
    @NotBlank String currency,
    @NotNull Boolean isDebit,
    String documentReference
) {}
