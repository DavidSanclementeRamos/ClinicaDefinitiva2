package com.example.ClinicaDefinitiva.application.dto.actor.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadPatientDto (
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
        Long userId,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
){}
