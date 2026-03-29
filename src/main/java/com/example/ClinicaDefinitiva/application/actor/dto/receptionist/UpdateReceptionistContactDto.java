
package com.example.ClinicaDefinitiva.application.actor.dto.receptionist;

public record UpdateReceptionistContactDto(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phoneNumber
) {
}
