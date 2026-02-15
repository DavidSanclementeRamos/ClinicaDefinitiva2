package com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateServiceInfoRequest(

        String name,
        Long categoryId,
        String categoryName,
        String categoryType,
        Integer durationMinutes,
        Boolean requiresAuthorization,
        String description
) {}

