package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO simplificado para listados
 */
public record JournalEntryListResponse(
        String id,
        LocalDate date,
        String documentNumber,
        String description,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        boolean posted
) {}
