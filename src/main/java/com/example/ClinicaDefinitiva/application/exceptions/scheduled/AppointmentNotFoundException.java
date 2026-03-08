package com.example.ClinicaDefinitiva.application.exceptions.scheduled;


public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
