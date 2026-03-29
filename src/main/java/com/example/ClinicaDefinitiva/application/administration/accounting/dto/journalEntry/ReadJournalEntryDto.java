package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de respuesta para JournalEntry
 */
public record ReadJournalEntryDto(
        Long id,
        Long companyId,
        LocalDate date,
        String documentNumber,
        String description,
        List<JournalEntryLineDto> lines,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        boolean balanced,
        boolean posted
) {}