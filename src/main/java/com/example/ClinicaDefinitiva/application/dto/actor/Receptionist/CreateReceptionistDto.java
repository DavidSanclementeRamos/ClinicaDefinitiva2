package com.example.ClinicaDefinitiva.application.dto.actor.Receptionist;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateReceptionistDto (
        String sector,
        Long shiftId,
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
){


}
