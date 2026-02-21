package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear un asiento accounting
 */
public record CreateJournalEntryDto(
        Long companyId,
        LocalDate date,
        String documentNumber,
        String description
) {}