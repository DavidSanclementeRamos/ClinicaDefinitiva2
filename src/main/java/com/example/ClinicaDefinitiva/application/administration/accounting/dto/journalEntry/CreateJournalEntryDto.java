package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

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