package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

import com.example.ClinicaDefinitiva.application.dto.AddressDto;
import com.example.ClinicaDefinitiva.application.dto.EmailDto;
import com.example.ClinicaDefinitiva.application.dto.NameDto;
import com.example.ClinicaDefinitiva.application.dto.PhoneNumberDto;

public record CreateThirdPartiesRequest(
        String companyId,
        NameDto name,
        String typeDocument,
        String documentNumber,
        String typeThirdParties,
        AddressDto address,
        PhoneNumberDto phoneNumber,
        EmailDto email
) {}
