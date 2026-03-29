package com.example.ClinicaDefinitiva.application.dentalService.dto;

import java.math.BigDecimal;

/**
 * DTO para actualizar la tarifa de un servicio.
 * Requiere justificación obligatoria según RN-SERVICE-008.
 */
public record UpdateServiceRateDto(
        BigDecimal newRateAmount,
        String currency,
        String justification
) {}
