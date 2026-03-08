package com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry;

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
