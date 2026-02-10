package com.example.ClinicaDefinitiva.application.dto.authentication;

public record UpdateUserIdentityDto(
        String email,
        String name,
        boolean verified,
        String password,
        long version
) {
}
