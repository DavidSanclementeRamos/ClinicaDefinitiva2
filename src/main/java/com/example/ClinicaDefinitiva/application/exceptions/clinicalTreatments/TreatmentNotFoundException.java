package com.example.ClinicaDefinitiva.application.exceptions.clinicalTreatments;

public class TreatmentNotFoundException extends RuntimeException {
    public TreatmentNotFoundException(String message) {
        super(message);
    }
}
