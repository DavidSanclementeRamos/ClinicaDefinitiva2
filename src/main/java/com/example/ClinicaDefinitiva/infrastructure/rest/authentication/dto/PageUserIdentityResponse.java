package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto;

public record PageUserIdentityResponse(
        Long id,
        String email,
        String name,
        boolean verified,

        String status
) {
}
