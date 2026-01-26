package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientCreateRequest(
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
