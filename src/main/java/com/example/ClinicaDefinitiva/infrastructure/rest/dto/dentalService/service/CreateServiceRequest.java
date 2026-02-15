package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Map;

public record CreateServiceRequest(

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
