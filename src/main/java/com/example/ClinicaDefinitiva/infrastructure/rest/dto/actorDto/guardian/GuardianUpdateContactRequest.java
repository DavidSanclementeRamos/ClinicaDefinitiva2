package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian;

public record GuardianUpdateContactRequest (
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {}

