package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import java.math.BigDecimal;

import java.time.LocalDate;

/**
 * DTO simplificado para listados
 */
public record PageContractDto(
        Long id,
        String name,
        Long thirdPartiesId,
        String coverageType,
        BigDecimal coverageRate,
        LocalDate endDate,
        ContractStatus status,
        boolean isExpired
) {}
