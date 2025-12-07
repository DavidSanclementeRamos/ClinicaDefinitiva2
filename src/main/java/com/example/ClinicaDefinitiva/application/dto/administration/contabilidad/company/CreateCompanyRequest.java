package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.application.dto.*;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.TypePerson;

import java.time.LocalDate;

/**
 * DTO para crear una nueva compañía
 */
public record CreateCompanyRequest(
        NameDto name,
        NitDto taxIdentificationNumber,
        TypePerson typePerson,
        TaxRegime taxRegime,
        String legalRepresentative,
        AddressDto address,
        PhoneNumberDto phoneNumber,
        EmailDto email
) {}