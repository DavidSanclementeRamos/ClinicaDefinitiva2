package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

public record CreateUserIdentityRequest(
        String email,
        String password,
        String name
) {
}
