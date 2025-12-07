package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry;

/**
 * DTO para actualizar asiento
 */
public record UpdateJournalEntryRequest(
        String description,
        String documentNumber
) {}
