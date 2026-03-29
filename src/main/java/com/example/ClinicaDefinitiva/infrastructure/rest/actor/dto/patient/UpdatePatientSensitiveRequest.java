package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient;

import java.time.LocalDate;

public record UpdatePatientSensitiveRequest(
        // Person
        String dni,
        String first,
        String lastName,
        String age, LocalDate dateOfBirth,
        String bloodType,
        String documentEPS
) {
}
