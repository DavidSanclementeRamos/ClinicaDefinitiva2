package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

public record PageUserIdentityResponse(
        Long id,
        String email,
        String name,
        boolean verified,

        String status
) {
}
