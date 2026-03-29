package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto;

public record CreateUserIdentityRequest(
        String email,
        String password,
        String name
) {
}
