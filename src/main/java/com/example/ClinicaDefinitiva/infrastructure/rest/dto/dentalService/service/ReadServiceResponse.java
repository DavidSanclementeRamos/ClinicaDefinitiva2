package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ReadServiceResponse(
        Long id,
        String code,
        String name,
        String description,
        BigDecimal rate,
        Integer durationMinutes,
        String category,
        String serviceType,
        String serviceDetails,
        Boolean requiresPreAuthorization,
        String contraindications,
        String status,

        BigDecimal previousRate
) {}
