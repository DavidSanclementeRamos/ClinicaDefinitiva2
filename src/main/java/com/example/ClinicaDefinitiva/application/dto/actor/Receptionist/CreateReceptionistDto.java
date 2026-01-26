package com.example.ClinicaDefinitiva.application.dto.actor.Receptionist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Sector;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateReceptionistDto (
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
){
}
