package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyStatus;

/**
 * DTO para actualizar estado
 */
public record UpdateCompanyStatusRequest(
        CompanyStatus.Status status
) {}
