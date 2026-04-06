package com.example.ClinicaDefinitiva.application.actor.dto.patient;

import java.util.Optional;

public record UpdatePatientContactDto(
    Optional<String> street,
    Optional<String> city,
    Optional<String> state,
    Optional<String> country,
    Optional<String> postalCode,
    Optional<String> phoneNumber
) {
    public UpdatePatientContactDto {
        street = street == null ? Optional.empty() : street;
        city = city == null ? Optional.empty() : city;
        state = state == null ? Optional.empty() : state;
        country = country == null ? Optional.empty() : country;
        postalCode = postalCode == null ? Optional.empty() : postalCode;
        phoneNumber = phoneNumber == null ? Optional.empty() : phoneNumber;
    }
}