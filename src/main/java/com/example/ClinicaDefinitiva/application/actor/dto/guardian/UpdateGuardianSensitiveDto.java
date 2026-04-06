package com.example.ClinicaDefinitiva.application.actor.dto.guardian;

import java.time.LocalDate;
import java.util.Optional;

public record UpdateGuardianSensitiveDto(
    Optional<String> dni,
    Optional<String> first,
    Optional<String> lastName,
    Optional<String> age,
    Optional<LocalDate> dateOfBirth,
    Optional<String> bloodType,
    Optional<String> documentEPS,
    Optional<String> code,
    Optional<String> description
) {
    public UpdateGuardianSensitiveDto {
        // Normalización: si algún valor es null, se convierte en Optional.empty()
        dni = dni == null ? Optional.empty() : dni;
        first = first == null ? Optional.empty() : first;
        lastName = lastName == null ? Optional.empty() : lastName;
        age = age == null ? Optional.empty() : age;
        dateOfBirth = dateOfBirth == null ? Optional.empty() : dateOfBirth;
        bloodType = bloodType == null ? Optional.empty() : bloodType;
        documentEPS = documentEPS == null ? Optional.empty() : documentEPS;
        code = code == null ? Optional.empty() : code;
        description = description == null ? Optional.empty() : description;
    }
}