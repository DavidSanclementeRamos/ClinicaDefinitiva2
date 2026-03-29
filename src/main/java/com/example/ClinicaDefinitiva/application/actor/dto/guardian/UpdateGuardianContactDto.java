
package com.example.ClinicaDefinitiva.application.actor.dto.guardian;

public record UpdateGuardianContactDto(
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {}
