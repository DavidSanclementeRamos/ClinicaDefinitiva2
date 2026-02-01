package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

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
