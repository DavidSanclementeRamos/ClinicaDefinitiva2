package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception;

public record UpdateReceptionistContactRequest(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
