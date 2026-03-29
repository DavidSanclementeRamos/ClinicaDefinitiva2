
package com.example.ClinicaDefinitiva.application.actor.dto.guardian;


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
