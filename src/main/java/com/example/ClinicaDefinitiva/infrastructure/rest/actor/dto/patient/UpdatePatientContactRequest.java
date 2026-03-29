package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient;

public record UpdatePatientContactRequest(
        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
