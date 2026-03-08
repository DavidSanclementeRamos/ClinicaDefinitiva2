package com.example.ClinicaDefinitiva.application.exceptions.actor;

public class DentistNotFoundException extends RuntimeException {
    public DentistNotFoundException(String message) {
        super(message);
    }
}

