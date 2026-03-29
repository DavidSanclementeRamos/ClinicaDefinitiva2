package com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties;

public record CreateThirdPartyDto(
        Long companyId,
        String name,
        String typeDocument,
        String documentNumber,
        String typeThirdParties,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,

        String phoneNumber,
        String email
) {}
