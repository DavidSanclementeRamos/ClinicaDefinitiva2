package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company;

import com.example.ClinicaDefinitiva.application.dto.AddressDto;
import com.example.ClinicaDefinitiva.application.dto.EmailDto;
import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.application.dto.PhoneNumberDto;


/**
 * DTO para actualizar información de contacto
 */
public record UpdateCompanyContactRequest(
        NameDto name,
        String legalRepresentative,
        AddressDto address,
        PhoneNumberDto phoneNumber,
        EmailDto email
) {}
