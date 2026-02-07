package com.example.ClinicaDefinitiva.application.dto.actor.guardian;

public record UpdateGuardianContactDto(
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {}
