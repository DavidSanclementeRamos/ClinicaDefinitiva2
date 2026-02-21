package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;

import java.time.LocalDate;

/**
 * DTO de respuesta para Contract
 */
public record ReadContractDto(
        Long id,
        Long companyId,
        Long thirdPartiesId,
        String name,
        String description,
        String origin,
        LocalDate startDate,
        LocalDate endDate,
        String coverageType,
        Double coverageRate,
        ContractStatus status,
        boolean isExpired,
        boolean isNearExpiration,
        Long daysRemaining
) {}