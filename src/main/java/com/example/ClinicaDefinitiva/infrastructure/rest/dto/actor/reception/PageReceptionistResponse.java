package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception;

public record PageReceptionistResponse(
        String sector,
        Long receptionist,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
