package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de respuesta para JournalEntry
 */
public record JournalEntryResponse(
        String id,
        String companyId,
        LocalDate date,
        String documentNumber,
        String description,
        List<JournalEntryLineResponse> lines,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        boolean balanced,
        boolean posted
) {}