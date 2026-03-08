package com.example.ClinicaDefinitiva.application.exceptions.administration.authorization;

public class UserRolAssignmentNotFoundException extends RuntimeException {
    public UserRolAssignmentNotFoundException(String message) {
        super(message);
    }
}
