package com.example.ClinicaDefinitiva.application.dto.authentication;

public record CreateUserIdentityDto(
        String email,
        String password,
        String name
) {
}
