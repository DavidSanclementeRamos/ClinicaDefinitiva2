
package com.example.ClinicaDefinitiva.application.actor.dto.dentist;

import java.time.LocalDate;
/**
 * DTO para actualizar información sensible
 */
public record UpdateDentistSensitiveDto(
       // Long dentistId,
        String specialties,
        
        WorkingHoursDto workingHoursDto,

        // Person
        String dni,
        String first,
        String lastName,
        String age, LocalDate dateOfBirth,
        String bloodType,
        String documentEPS
) {
}
