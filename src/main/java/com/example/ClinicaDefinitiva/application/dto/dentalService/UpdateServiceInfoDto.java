package com.example.ClinicaDefinitiva.application.dto.dentalService;

/**
 * DTO para actualizar información básica de un servicio.
 * No incluye cambios de tarifa (que requieren justificación).
 */
public record UpdateServiceInfoDto(
        String name,
        String categoryId,
        String categoryName,
        String categoryType,
        Integer durationMinutes,
        Boolean requiresAuthorization,
        String description
) {}
