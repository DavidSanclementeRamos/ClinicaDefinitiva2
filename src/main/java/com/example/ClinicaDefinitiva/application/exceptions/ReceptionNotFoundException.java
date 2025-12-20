package com.example.ClinicaDefinitiva.application.exceptions;

public class ReceptionNotFoundException extends RuntimeException {
    public ReceptionNotFoundException(String message) {
        super(message);
    }
}
