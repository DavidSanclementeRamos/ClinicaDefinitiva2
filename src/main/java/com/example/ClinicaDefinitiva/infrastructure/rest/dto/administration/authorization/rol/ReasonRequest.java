package com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReasonRequest(
        @NotBlank(message = "El motivo es obligatorio")
        @Size(min = 10, message = "El motivo debe tener al menos 10 caracteres")
        String reason
) {
}
