package com.example.ClinicaDefinitiva.application.dto.authentication;

public record PageUserIdentityDto(
        Long id,
        String email,
        String name,
        boolean verified,
        String status

) {
}
