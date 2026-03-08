package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO para crear un nuevo Odontólogo
 */
public record CreateDentistRequest(

     String specialties,
     String availabilityStatus,

     WorkingHoursRequest WorkingHours,
    

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
