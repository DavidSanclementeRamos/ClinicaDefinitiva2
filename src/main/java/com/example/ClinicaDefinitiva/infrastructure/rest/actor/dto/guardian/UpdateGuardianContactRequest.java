package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian;

public record UpdateGuardianContactRequest(
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {}

