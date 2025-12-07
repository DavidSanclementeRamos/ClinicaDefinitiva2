package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;


import com.example.ClinicaDefinitiva.application.dto.*;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyStatus;


import java.time.LocalDate;

/**
 * DTO de respuesta para Company
 */
public record CompanyResponse(
        String id,
        NameDto name,
        NitDto taxIdentificationNumber,
        TypePerson typePerson,
        TaxRegime taxRegime,
        String legalRepresentative,
        AddressDto address,
        PhoneNumberDto phoneNumber,
        EmailDto email,
        LocalDate incorporationDate,
        CompanyStatus.Status status
) {}