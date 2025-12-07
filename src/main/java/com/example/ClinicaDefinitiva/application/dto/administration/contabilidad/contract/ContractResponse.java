package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;

import java.time.LocalDate;

/**
 * DTO de respuesta para Contract
 */
public record ContractResponse(
        String id,
        String companyId,
        String thirdPartiesId,
        String thirdPartiesName,
        NameDto name,
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