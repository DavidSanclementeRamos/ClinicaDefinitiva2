package com.example.ClinicaDefinitiva.application.dto.administration.accounting.thirdParties;

public record ReadThirdPartyDto(
        Long id,
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
        String email,
        boolean active
) {}
