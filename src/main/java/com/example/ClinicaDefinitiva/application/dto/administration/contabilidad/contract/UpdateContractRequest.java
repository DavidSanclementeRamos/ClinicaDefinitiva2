package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import com.example.ClinicaDefinitiva.application.dto.NameDto;

/**
 * DTO para actualizar información del contrato
 */
public record UpdateContractRequest(
        NameDto name,
        String description,
        String origin,
        String coverageType
) {}