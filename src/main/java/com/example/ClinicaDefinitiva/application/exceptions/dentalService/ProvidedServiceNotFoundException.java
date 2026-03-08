package com.example.ClinicaDefinitiva.application.exceptions;

public class ProvidedServiceNotFoundException extends RuntimeException {
    public ProvidedServiceNotFoundException(String message) {
        super(message);
    }
}
