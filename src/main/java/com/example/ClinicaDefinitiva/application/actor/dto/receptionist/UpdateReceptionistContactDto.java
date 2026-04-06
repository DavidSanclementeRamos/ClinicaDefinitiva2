package com.example.ClinicaDefinitiva.application.actor.dto.receptionist;

import java.util.Optional;

public record UpdateReceptionistContactDto(
    Optional<String> street,
    Optional<String> city,
    Optional<String> state,
    Optional<String> country,
    Optional<String> postalCode,
    Optional<String> phoneNumber
) {
    public UpdateReceptionistContactDto {
        street = street == null ? Optional.empty() : street;
        city = city == null ? Optional.empty() : city;
        state = state == null ? Optional.empty() : state;
        country = country == null ? Optional.empty() : country;
        postalCode = postalCode == null ? Optional.empty() : postalCode;
        phoneNumber = phoneNumber == null ? Optional.empty() : phoneNumber;
    }
}
