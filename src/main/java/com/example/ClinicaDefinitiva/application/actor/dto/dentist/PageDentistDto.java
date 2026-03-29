
package com.example.ClinicaDefinitiva.application.actor.dto.dentist;

public record PageDentistDto(
        Long dentistId,
        String specialties,
        String dni,
        String first,
        String lastName,
        String phoneNumber,
        String availabilityStatus
) {
}
