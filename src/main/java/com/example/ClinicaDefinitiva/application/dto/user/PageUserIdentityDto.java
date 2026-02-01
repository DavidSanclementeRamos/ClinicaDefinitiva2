package com.example.ClinicaDefinitiva.application.dto.user;

public record PageUserIdentityDto(
        Long id,
        String email,
        String name,
        boolean verified,

        String status
) {
}
