package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception;

import java.time.LocalDate;

public record ReceptionUpdateSensitiveRequest(
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
