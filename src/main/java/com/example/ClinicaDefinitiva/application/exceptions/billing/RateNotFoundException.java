package com.example.ClinicaDefinitiva.application.exceptions.billing;

public class RateNotFoundException extends RuntimeException {
    public RateNotFoundException(String message) {
        super(message);
    }
}
