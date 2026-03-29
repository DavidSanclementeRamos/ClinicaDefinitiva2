package com.example.ClinicaDefinitiva.infrastructure.rest.dentalService.dto;

public record UpdateServiceInfoRequest(

        String name,
        Long categoryId,
        String categoryName,
        String categoryType,
        Integer durationMinutes,
        Boolean requiresAuthorization,
        String description
) {}

