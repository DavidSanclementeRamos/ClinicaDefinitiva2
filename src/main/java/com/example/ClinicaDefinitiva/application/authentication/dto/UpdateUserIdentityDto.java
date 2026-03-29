package com.example.ClinicaDefinitiva.application.authentication.dto;

public record UpdateUserIdentityDto(
        String email,
        String name,
        String password
) {
}
