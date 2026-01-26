package com.example.ClinicaDefinitiva.application.dto.actor.Receptionist;

public record UpdateReceptionistContactDto(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
