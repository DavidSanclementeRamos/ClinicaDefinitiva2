package com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry;

import java.time.LocalDate;

/**
 * DTO para crear un asiento accounting
 */
public record CreateJournalEntryDto(
        Long companyId,
        LocalDate date,
        String documentNumber,
        String description
) {}