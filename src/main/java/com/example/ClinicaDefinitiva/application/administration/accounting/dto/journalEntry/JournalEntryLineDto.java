package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;

public record JournalEntryLineDto(
        Long id,
        Long thirdPartyId,
        String description,
        BigDecimal amount,
        String  document,
        boolean isDebit
) {
}
