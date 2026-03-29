package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;

public record JournalEntryLineResponse(
    Long accountId,
    Long thirdPartyId,
    String description,
    BigDecimal amount,
    boolean isDebit,
    String documentReference
) {}
