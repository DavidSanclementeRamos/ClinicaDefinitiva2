package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto;

public record UpdateUserIdentityRequest(
        String email,
        String name,
        String password
) {
}
