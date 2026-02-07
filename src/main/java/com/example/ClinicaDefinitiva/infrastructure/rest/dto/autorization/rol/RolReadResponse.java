package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol;

import java.util.Set;

public record RolReadResponse(
        Long id,
        String rolEnum,
        String description,
        boolean isDefault,
        boolean isEditable,
        boolean isDeletable,
        String statusRol,
        Set<String> permissions
) {
}
