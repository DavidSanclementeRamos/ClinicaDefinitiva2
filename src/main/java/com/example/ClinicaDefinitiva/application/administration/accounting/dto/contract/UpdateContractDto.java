package com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract;

/**
 * DTO para actualizar información del contrato
 */
public record UpdateContractDto(
        String name,
        String description,
        String origin,
        String coverageType
) {}