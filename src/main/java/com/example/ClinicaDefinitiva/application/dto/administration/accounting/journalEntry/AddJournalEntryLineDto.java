package com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry;

import java.math.BigDecimal;

/**
 * DTO de respuesta para línea de asiento
 */
public record AddJournalEntryLineDto(
        Long id,
        Long thirdPartyId,
        String description,
        BigDecimal amount,
        String  document,
        boolean isDebit
) {}
