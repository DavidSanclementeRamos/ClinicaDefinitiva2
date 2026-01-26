package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReceptionReadResponse(
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
        String user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {
}
