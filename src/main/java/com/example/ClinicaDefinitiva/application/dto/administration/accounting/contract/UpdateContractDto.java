package com.example.ClinicaDefinitiva.application.dto.administration.accounting.contract;

/**
 * DTO para actualizar información del contrato
 */
public record UpdateContractDto(
        String name,
        String description,
        String origin,
        String coverageType
) {}