package com.example.ClinicaDefinitiva.application.exceptions.administration.accounting;

public class JournalEntryNotFoundException extends RuntimeException {
    public JournalEntryNotFoundException(String message) {
        super(message);
    }
}
