package com.example.ClinicaDefinitiva.application.dto.administration.accounting.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;

/**
 * DTO simplificado para listados
 */
public record PageCompanyDto(
        Long id,
        String name,
        String taxIdentificationNumber,
        String status
) {}