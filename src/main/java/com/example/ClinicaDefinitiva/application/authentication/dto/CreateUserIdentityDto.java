package com.example.ClinicaDefinitiva.application.authentication.dto;

public record CreateUserIdentityDto(
        String email,
        String password,
        String name
) {
}
