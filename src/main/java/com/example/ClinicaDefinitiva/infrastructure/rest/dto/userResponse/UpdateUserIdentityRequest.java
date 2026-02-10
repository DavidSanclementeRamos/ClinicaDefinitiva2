package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

public record UpdateUserIdentityRequest(
        String email,
        String name,
        boolean verified,
        String status,
        String password,
        long version
) {
}
