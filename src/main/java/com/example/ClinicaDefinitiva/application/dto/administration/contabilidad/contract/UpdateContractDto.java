package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

/**
 * DTO para actualizar información del contrato
 */
public record UpdateContractDto(
        String name,
        String description,
        String origin,
        String coverageType
) {}