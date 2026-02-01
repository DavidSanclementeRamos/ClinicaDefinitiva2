package com.example.ClinicaDefinitiva.application.dto.user;

public record CreateUserIdentityDto(
        String email,
        String password,
        String name
) {
}
