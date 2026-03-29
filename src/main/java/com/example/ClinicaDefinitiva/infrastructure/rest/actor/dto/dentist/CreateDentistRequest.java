package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;


import java.time.LocalDate;
import java.time.LocalDateTime;

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
