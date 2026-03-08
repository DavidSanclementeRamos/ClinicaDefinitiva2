package com.example.ClinicaDefinitiva.application.exceptions;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
