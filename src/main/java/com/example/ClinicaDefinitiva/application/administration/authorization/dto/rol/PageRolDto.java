package com.example.ClinicaDefinitiva.application.administration.authorization.dto.rol;

public record PageRolDto(
        Long id,
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable,
        String statusRol
) {
}
