package com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol;

public record CreateRolDto(
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable
) {
}
