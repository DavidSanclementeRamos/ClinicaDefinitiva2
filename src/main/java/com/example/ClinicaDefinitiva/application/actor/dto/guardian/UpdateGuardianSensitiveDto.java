
package com.example.ClinicaDefinitiva.application.actor.dto.guardian;

import java.time.LocalDate;

public record UpdateGuardianSensitiveDto(
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