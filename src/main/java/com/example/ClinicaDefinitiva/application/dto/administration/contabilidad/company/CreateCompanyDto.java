package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;


import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;

/**
 * DTO para crear una nueva compañía
 */
public record CreateCompanyDto(
        String name,
        String taxIdentificationNumber,
        String typePerson,
        String taxRegime,
        String legalRepresentative,

        // ADDRESS
        String street,
        String city,
        String state,
        String country,
        String postalCode,

        String phoneNumber,
        String email
) {}