package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.rol;

import jakarta.validation.constraints.NotBlank;

public record CloneRoleRequest(
    @NotBlank(message = "La descripción del rol clonado es obligatoria")
    String description
) {}
