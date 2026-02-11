package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;


import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;

/**
 * DTO para crear una nueva compañía
 */
public record CreateCompanyRequest(
        //NameDto name,
        //NitDto taxIdentificationNumber,
        TypePerson typePerson,
        TaxRegime taxRegime,
        String legalRepresentative
        //AddressDto address,
        //PhoneNumberDto phoneNumber,
        //EmailDto email
) {}