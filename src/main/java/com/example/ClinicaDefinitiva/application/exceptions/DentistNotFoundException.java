package com.example.ClinicaDefinitiva.application.exceptions;

public class DentistNotFoundException extends RuntimeException {
    public DentistNotFoundException(String message) {
        super(message);
    }
}
