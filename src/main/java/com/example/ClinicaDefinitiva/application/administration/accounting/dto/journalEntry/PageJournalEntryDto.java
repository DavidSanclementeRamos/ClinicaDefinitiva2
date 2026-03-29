package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO simplificado para listados
 */
public record PageJournalEntryDto(
        Long id,
        LocalDate date,
        String documentNumber,
        String description,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        boolean posted
) {}
