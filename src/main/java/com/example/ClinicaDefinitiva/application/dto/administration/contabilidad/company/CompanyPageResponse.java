package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.application.dto.NitDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyStatus;

/**
 * DTO simplificado para listados
 */
public record CompanyPageResponse(
        String id,
        NameDto name,
        NitDto taxIdentificationNumber,
        CompanyStatus.Status status
) {}