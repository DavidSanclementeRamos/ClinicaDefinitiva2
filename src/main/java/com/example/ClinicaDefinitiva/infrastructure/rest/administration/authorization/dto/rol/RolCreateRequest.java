package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol;

public record RolCreateRequest(
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable
) {
}
