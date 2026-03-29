
package com.example.ClinicaDefinitiva.application.actor.dto.dentist;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record CreateDentistDto (
        String specialties,
        String availabilityStatus,

        WorkingHoursDto workingHoursDto,

        // Person
        String dni,
        String first,
        String lastName,
        String age,
        String phoneNumber,

        LocalDate dateOfBirth,
        String bloodType,
        String documentoEPS,
        Long user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
){

}
