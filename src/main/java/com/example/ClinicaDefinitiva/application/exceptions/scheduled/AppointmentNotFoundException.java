package com.example.ClinicaDefinitiva.application.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
