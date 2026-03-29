package com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import java.math.BigDecimal;

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
        BigDecimal coverageRate,
        ContractStatus status,
        boolean isExpired,
        boolean isNearExpiration,
        Long daysRemaining
) {}