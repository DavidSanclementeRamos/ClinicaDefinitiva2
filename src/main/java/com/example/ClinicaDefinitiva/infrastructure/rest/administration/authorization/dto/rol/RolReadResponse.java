package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol;

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
