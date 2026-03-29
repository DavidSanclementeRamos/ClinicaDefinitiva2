package com.example.ClinicaDefinitiva.application.administration.accounting.dto.company;


import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;


import java.time.LocalDate;

/**
 * DTO de respuesta para Company
 */
public record ReadCompanyDto(
        Long id,
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
        String email,
        LocalDate incorporationDate,
        String status
) {}