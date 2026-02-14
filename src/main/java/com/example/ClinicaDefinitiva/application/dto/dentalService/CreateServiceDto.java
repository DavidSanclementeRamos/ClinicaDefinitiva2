package com.example.ClinicaDefinitiva.application.dto.dentalService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO para la creación de un nuevo servicio odontológico.
 * Incluye toda la información requerida, con detalles específicos
 * representados como un mapa flexible.
 */
public record CreateServiceDto(
        String name,
        Long categoryId,
        String categoryName,
        String categoryType,
        String code,
        BigDecimal baseRateAmount,
        String currency,
        Integer durationMinutes,
        boolean requiresAuthorization,
        String description,
        String serviceType,

        // Detalles específicos según el tipo de servicio
        Map<String, Object> details
) {}

