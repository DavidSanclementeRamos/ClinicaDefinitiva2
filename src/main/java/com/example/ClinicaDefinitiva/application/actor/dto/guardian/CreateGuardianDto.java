
package com.example.ClinicaDefinitiva.application.actor.dto.guardian;

import com.example.ClinicaDefinitiva.application.actor.dto.patient.PagePatientDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateGuardianDto(

        //TypeGuardian
        String code,
        String description,

        // Person
        String dni,
        String first,
        String lastName,
        String age,
        String phoneNumber,

        LocalDate dateOfBirth,
        String bloodType,
        String documentEPS,
        Long userId,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {}
