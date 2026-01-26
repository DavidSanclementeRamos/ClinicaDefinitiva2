package com.example.ClinicaDefinitiva.application.dto.actor.Receptionist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Sector;

public record PageReceptionistDto(
        String sector,
        Long receptionist,
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
