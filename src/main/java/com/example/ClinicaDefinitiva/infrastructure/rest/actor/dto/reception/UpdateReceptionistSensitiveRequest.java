package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception;

import java.time.LocalDate;

public record UpdateReceptionistSensitiveRequest(
        String sector,
        // Person
        String dni,
        String first,
        String lastName,
        String age,
        LocalDate dateOfBirth,
        String bloodType,
        String documentEPS) {
}
