package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.application.dto.NitDto;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TypePerson;

import java.time.LocalDate;

/**
 * DTO para actualizar información fiscal
 */
public record UpdateCompanyTaxRequest(
        NitDto taxIdentificationNumber,
        TaxRegime taxRegime,
        TypePerson typePerson,
        LocalDate incorporationDate
) {}

