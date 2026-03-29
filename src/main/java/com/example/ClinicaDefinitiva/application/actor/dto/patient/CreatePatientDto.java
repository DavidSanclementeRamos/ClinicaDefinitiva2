
package com.example.ClinicaDefinitiva.application.actor.dto.patient;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreatePatientDto(

     Long guardianId,
     Long contractId,
     Long userId,
     // Person
     String dni,
     String first,
     String lastName,
     String age,
     String phoneNumber,

     LocalDate dateOfBirth,
     String bloodType,
     String documentEPS,
     LocalDateTime lastUpdate,

     // Address
     String street,
     String city,
     String state,
     String country,
     String postalCode
     ){}
