package com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear un nuevo contrato
 */
public record CreateContractDto(
        Long companyId,
        Long thirdPartiesId,
        String name,
        String description,
        String origin,
        LocalDate endDate,
        String coverageType,
        BigDecimal coverageRate
) {}