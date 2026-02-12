package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient;

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
