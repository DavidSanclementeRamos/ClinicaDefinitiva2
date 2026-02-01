package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

public record UserIdentityCreateRequest(
        String email,
        String password,
        String name
) {
}
