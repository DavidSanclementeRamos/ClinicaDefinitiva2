package com.example.ClinicaDefinitiva.application.authentication.dto;

public record PageUserIdentityDto(
        Long id,
        String email,
        String name,
        boolean verified,
        String status

) {
}
