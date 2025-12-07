package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.ContractStatus;

import java.time.LocalDate;

/**
 * DTO simplificado para listados
 */
public record ContractPageResponse(
        String id,
        NameDto name,
        String thirdPartiesName,
        String coverageType,
        Double coverageRate,
        LocalDate endDate,
        ContractStatus status,
        boolean isExpired
) {}
