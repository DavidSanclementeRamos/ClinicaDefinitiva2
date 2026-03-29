package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto;

import java.math.BigDecimal;


public record PageServiceResponse(
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

