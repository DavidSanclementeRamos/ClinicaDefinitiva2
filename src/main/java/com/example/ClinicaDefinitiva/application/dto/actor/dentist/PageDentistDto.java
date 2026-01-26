package com.example.ClinicaDefinitiva.application.dto.actor.dentist;

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
