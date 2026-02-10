package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;

/**
 * DTO simplificado para listados
 */
public record CompanyPageResponse(
        String id,
        //NameDto name,
        //NitDto taxIdentificationNumber,
        CompanyStatus.Status status
) {}