package com.example.ClinicaDefinitiva.application.dentalService.dto;

import java.math.BigDecimal;

/**
 * DTO para listados paginados de servicios.
 * Contiene información mínima para vistas de lista.
 */
public record PageServiceDto(
        Long id,
        String name,
        String category,
        String code,
        BigDecimal baseRate,
        String currency,
        Integer durationMinutes,
        String status,
        String serviceType
) {}