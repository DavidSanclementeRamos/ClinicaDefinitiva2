package com.example.ClinicaDefinitiva.application.actor.dto.dentist;

import java.time.LocalDate;
import java.util.Optional;

public record UpdateDentistSensitiveDto(
    Optional<String> specialties,
    Optional<WorkingHoursDto> workingHoursDto,
    Optional<String> dni,
    Optional<String> first,
    Optional<String> lastName,
    Optional<String> age,
    Optional<LocalDate> dateOfBirth,
    Optional<String> bloodType,
    Optional<String> documentEPS
) {
    public UpdateDentistSensitiveDto {
        specialties = specialties == null ? Optional.empty() : specialties;
        workingHoursDto = workingHoursDto == null ? Optional.empty() : workingHoursDto;
        dni = dni == null ? Optional.empty() : dni;
        first = first == null ? Optional.empty() : first;
        lastName = lastName == null ? Optional.empty() : lastName;
        age = age == null ? Optional.empty() : age;
        dateOfBirth = dateOfBirth == null ? Optional.empty() : dateOfBirth;
        bloodType = bloodType == null ? Optional.empty() : bloodType;
        documentEPS = documentEPS == null ? Optional.empty() : documentEPS;
    }
}
