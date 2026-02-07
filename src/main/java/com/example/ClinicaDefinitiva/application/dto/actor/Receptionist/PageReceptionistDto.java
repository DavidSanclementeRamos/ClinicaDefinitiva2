package com.example.ClinicaDefinitiva.application.dto.actor.Receptionist;

public record PageReceptionistDto(
        String sector,
        Long receptionist,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
