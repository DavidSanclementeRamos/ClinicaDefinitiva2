package com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol;

import java.util.Set;

public record ReadRolDto(
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
