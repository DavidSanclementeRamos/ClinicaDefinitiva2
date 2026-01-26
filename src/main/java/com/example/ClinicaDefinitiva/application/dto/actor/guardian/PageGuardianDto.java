package com.example.ClinicaDefinitiva.application.dto.actor.guardian;

public record PageGuardianDto(
        Long guardianId,
        // TypeGuardian
        String code,
        String description,

        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
