package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
/**
 * DTO para actualizar información sensible
 */
public record UpdateDentistSensitiveRequest(
        
         @NotBlank String specialties,
    @Valid @NotNull WorkingHoursRequest workingHours,  // ← Campo anidado
    @NotBlank @Pattern(regexp = "^\\d{6,10}$") String dni,
    @NotBlank String first,
    @NotBlank String lastName,
    @NotBlank String age,
    @NotNull @Past LocalDate dateOfBirth,
    @NotBlank String bloodType,
    @NotBlank String documentoEPS){
}
