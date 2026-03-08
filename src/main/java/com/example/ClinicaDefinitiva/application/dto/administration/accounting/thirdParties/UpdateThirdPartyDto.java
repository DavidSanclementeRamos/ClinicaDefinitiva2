package com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties;

public record UpdateThirdPartyDto(
        String name,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,

        String phoneNumber,
        String email
) {}
