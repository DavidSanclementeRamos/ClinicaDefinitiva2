package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;

import java.time.LocalDate;

/**
 * DTO para actualizar información fiscal
 */
public record UpdateCompanyTaxDto(
        String taxIdentificationNumber,
        String taxRegime,
        String typePerson,
        LocalDate incorporationDate
) {}

