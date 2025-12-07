package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry;

import java.math.BigDecimal;

/**
 * DTO de respuesta para línea de asiento
 */
public record JournalEntryLineResponse(
        String ledgerAccountId,
        String ledgerAccountCode,
        String ledgerAccountName,
        String thirdPartiesId,
        String thirdPartiesName,
        String description,
        BigDecimal amount,
        String currency,
        boolean isDebit,
        String movementType,
        String documentReference
) {}
