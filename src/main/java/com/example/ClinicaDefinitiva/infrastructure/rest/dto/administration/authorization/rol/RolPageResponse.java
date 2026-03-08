package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol;

public record RolPageResponse(
        Long id,
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable,
        String statusRol
) {
}
