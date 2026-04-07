package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
/**
 * DTO para actualizar información sensible de forma parcial
 */
public record UpdateDentistSensitiveRequest(
        
          String specialties,
      WorkingHoursRequest workingHours,  
      String dni,
     String first,
     String lastName,
     String age,
     @Past LocalDate dateOfBirth,
     String bloodType,
     String documentoEPS){
}
