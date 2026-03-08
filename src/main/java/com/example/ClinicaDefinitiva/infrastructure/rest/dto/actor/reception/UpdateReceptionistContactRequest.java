package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception;

public record UpdateReceptionistContactRequest(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
