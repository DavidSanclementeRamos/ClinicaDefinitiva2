package com.example.ClinicaDefinitiva.application.dto.dentalService;

import java.util.Map;

/**
 * DTO para actualizar los detalles específicos de un servicio.
 * La estructura del mapa depende del tipo de servicio.
 * Regla de negocio: RN-SERVICE-006 - El tipo de servicio no puede cambiar.
 */
public record UpdateServiceDetailsDto(
        String serviceType,
        Map<String, Object> details
) {}