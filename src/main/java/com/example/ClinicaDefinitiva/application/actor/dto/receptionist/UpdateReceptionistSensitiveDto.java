
package com.example.ClinicaDefinitiva.application.actor.dto.receptionist;

import java.time.LocalDate;

public record UpdateReceptionistSensitiveDto (
        // Person
        String dni,
        String first,
        String lastName,
        String age,
        LocalDate dateOfBirth,
        String bloodType,
        String documentEPS,
        String sector
){
}
