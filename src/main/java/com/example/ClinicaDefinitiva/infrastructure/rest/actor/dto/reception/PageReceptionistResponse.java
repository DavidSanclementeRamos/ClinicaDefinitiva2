package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception;

public record PageReceptionistResponse(
        Long receptionist,
        String sector,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
