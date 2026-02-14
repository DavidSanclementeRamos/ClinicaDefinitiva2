package com.example.ClinicaDefinitiva.application.dto.dentalService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de lectura de un servicio odontológico.
 * Contiene toda la información detallada de un servicio,
 * incluyendo metadatos y detalles específicos en formato flexible.
 */
public record ReadServiceDto(
        Long id,
        String name,
        String category,
        String code,
        BigDecimal baseRate,
        String currency,
        Integer durationMinutes,
        boolean requiresAuthorization,
        String description,
        String status,
        String serviceType,

        // Detalles específicos del servicio (serializados como JSON)
        String detailsJson,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

