package com.example.ClinicaDefinitiva.application.dto.administration.accounting.journalEntry;

/**
 * DTO para actualizar asiento
 */
public record UpdateJournalEntryDto(
        String description,
        String documentNumber
) {}
