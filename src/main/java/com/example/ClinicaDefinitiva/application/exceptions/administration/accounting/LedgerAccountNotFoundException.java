package com.example.ClinicaDefinitiva.application.exceptions.administration.accounting;

public class LedgerAccountNotFoundException extends RuntimeException {
    public LedgerAccountNotFoundException(String message) {
        super(message);
    }
}
