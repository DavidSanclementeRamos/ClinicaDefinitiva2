package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;


import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;


import java.time.LocalDate;

/**
 * DTO de respuesta para Company
 */
public record CompanyResponse(
        String id,
        //NameDto name,
        //NitDto taxIdentificationNumber,
        TypePerson typePerson,
        TaxRegime taxRegime,
        String legalRepresentative,
        //AddressDto address,
        //PhoneNumberDto phoneNumber,
        //EmailDto email,
        LocalDate incorporationDate,
        CompanyStatus.Status status
) {}