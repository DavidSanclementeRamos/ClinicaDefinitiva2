package com.example.ClinicaDefinitiva.application.exceptions.administration.authorization;

public class RolNotFoundException extends RuntimeException {
    public RolNotFoundException(String message) {
        super(message);
    }
}
