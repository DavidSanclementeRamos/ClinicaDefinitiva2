package com.example.ClinicaDefinitiva.application.exceptions;

public class TreatmentNotFoundException extends RuntimeException {
    public TreatmentNotFoundException(String message) {
        super(message);
    }
}
