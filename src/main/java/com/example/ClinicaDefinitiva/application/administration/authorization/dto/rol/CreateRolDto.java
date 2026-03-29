package com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol;

public record CreateRolDto(
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable
) {
}
