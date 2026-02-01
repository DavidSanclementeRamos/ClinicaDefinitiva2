package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

/**
 * DTO para actualizar información del contrato
 */
public record UpdateContractRequest(
        NameDto name,
        String description,
        String origin,
        String coverageType
) {}