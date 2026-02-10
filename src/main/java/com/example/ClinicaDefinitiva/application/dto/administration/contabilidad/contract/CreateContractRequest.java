package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.contract;

import java.time.LocalDate;

/**
 * DTO para crear un nuevo contrato
 */
public record CreateContractRequest(
        String companyId,
        String thirdPartiesId,
        //NameDto name,
        String description,
        String origin,
        LocalDate endDate,
        String coverageType,
        Double coverageRate
) {}