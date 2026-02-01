package com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse;

public record UserIdentityPageResponse(
        Long id,
        String email,
        String name,
        boolean verified,

        String status
) {
}
