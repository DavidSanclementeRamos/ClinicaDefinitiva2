package com.example.ClinicaDefinitiva.application.exceptions.administration.accounting;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
