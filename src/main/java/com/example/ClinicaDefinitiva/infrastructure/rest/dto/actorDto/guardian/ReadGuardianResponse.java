package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReadGuardianResponse(
        Long guardianId,
        // TypeGuardian
        String code,
        String description,

        List<String> patientList,
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
) {}

