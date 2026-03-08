package com.example.ClinicaDefinitiva.application.exceptions.billing;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
