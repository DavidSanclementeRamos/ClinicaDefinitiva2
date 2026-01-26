package com.example.ClinicaDefinitiva.application.dto.actor.Patient;

import java.time.LocalDate;

public record UpdatePatientSensitiveDto (
        // Person
        String dni,
        String first,
        String lastName,
        String age, LocalDate dateOfBirth,
        String bloodType,
        String documentEPS
) {}



