package com.example.ClinicaDefinitiva.application.exceptions.actorException;

public class DentistNotFoundException extends RuntimeException {
    public DentistNotFoundException(String message) {
        super(message);
    }
}
