package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol;

public record PermissionRequest(
        String actionCode,
        String resourceCode
) {
}
