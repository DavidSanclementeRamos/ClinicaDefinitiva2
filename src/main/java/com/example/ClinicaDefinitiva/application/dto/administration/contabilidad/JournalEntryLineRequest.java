package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad;

import java.math.BigDecimal;

/**
 * DTO para línea de asiento
 */
public record JournalEntryLineRequest(
        String ledgerAccountId,
        String ledgerAccountCode,
        String thirdPartiesId,
        String description,
        BigDecimal amount,
        String currency,
        boolean isDebit,
        String documentReference
) {}
