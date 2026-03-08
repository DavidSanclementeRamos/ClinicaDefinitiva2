package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian;

import java.time.LocalDate;

public record UpdateGuardianSensitiveRequest(
        // Person
        String dni,
        String first,
        String lastName,
        String age, LocalDate dateOfBirth,
        String bloodType,
        String documentEPS,
        // TypeGuardian
        String code,
        String description
) {
}

