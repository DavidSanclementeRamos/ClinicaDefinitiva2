package com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry;

/**
 * DTO para actualizar asiento
 */
public record UpdateJournalEntryDto(
        String description,
        String documentNumber
) {}
