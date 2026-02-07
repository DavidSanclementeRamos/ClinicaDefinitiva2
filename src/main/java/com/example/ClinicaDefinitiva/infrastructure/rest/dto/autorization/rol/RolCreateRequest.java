package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol;

public record RolCreateRequest(
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable
) {
}
