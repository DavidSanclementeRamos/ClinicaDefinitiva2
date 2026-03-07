package com.example.ClinicaDefinitiva.application.dto.actor.dentist;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReadDentistDto(
        Long dentistId,
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
        String user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {

}


