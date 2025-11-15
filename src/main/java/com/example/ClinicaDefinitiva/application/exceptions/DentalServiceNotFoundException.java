package com.example.ClinicaDefinitiva.application.exceptions;

public class DentalServiceNotFoundException extends RuntimeException {
    public DentalServiceNotFoundException(String message) {
        super(message);
    }
}
