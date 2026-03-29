package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadReceptionistResponse(
        Long receptionist,
        String sector,
        // Person
        String dni,
        String first,
        String lastName,
        String age,
        String phoneNumber,

        LocalDate dateOfBirth,
        String bloodType,
        String documentEPS,
        Long user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {
}
