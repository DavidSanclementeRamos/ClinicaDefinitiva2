package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto;


import java.math.BigDecimal;


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
