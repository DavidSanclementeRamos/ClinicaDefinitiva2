
package com.example.ClinicaDefinitiva.application.actor.dto.receptionist;

public record PageReceptionistDto(
        Long receptionist,
        String sector,
        
        String dni,
        String first,
        String lastName,
        String phoneNumber
) {
}
