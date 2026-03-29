package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateServiceRateRequest(

        @NotNull(message = "La nueva tarifa es requerida")
        @DecimalMin(value = "0.01", message = "La tarifa debe ser mayor a 0")
        BigDecimal newRate,

        String currency,
        @NotBlank(message = "La justificación es obligatoria para cambios de tarifa")
        @Size(min = 10, max = 500, message = "La justificación debe tener entre 10 y 500 caracteres")
        String justification
) {}

