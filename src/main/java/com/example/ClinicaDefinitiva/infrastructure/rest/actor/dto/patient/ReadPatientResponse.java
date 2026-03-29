package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadPatientResponse(
        Long patientId,
        Long guardianId,
        Long contractId,
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
