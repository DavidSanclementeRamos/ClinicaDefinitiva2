package com.example.ClinicaDefinitiva.application.exceptions.authentication;


public class UserIdentityNoFoundException extends RuntimeException {
    public UserIdentityNoFoundException(String message) {
        super(message);
    }
}