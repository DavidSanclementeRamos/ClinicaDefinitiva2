package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service;

import jakarta.validation.constraints.Size;

import java.util.Map;

public record UpdateServiceDetailsRequest(

        @Size(max = 1000, message = "Los detalles no pueden exceder 1000 caracteres")
        String serviceDetails,

        // Campos específicos según el tipo de servicio
        Map<String, Object> additionalDetails
) {}

