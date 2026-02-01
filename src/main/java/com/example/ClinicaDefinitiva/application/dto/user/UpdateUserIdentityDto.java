package com.example.ClinicaDefinitiva.application.dto.user;

public record UpdateUserIdentityDto(
        String email,
        String name,
        boolean verified,
        String status,
        String password,
        long version
) {
}
