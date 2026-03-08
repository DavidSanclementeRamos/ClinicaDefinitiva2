package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol;

public record PermissionRequest(
        String actionCode,
        String resourceCode
) {
}
