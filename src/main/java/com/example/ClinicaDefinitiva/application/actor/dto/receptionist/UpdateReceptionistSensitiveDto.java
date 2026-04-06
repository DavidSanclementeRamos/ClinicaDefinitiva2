package com.example.ClinicaDefinitiva.application.actor.dto.receptionist;

import java.time.LocalDate;
import java.util.Optional;

public record UpdateReceptionistSensitiveDto(
    Optional<String> dni,
    Optional<String> first,
    Optional<String> lastName,
    Optional<String> age,
    Optional<LocalDate> dateOfBirth,
    Optional<String> bloodType,
    Optional<String> documentEPS,
    Optional<String> sector
) {
    public UpdateReceptionistSensitiveDto {
        dni = dni == null ? Optional.empty() : dni;
        first = first == null ? Optional.empty() : first;
        lastName = lastName == null ? Optional.empty() : lastName;
        age = age == null ? Optional.empty() : age;
        dateOfBirth = dateOfBirth == null ? Optional.empty() : dateOfBirth;
        bloodType = bloodType == null ? Optional.empty() : bloodType;
        documentEPS = documentEPS == null ? Optional.empty() : documentEPS;
        sector = sector == null ? Optional.empty() : sector;
    }
}