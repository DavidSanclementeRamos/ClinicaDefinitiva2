package com.example.ClinicaDefinitiva.application.dto.actor.guardian;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.PagePatientDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateGuardianDto(

        //TypeGuardian
        String code,
        String description,

        Page<PagePatientDto> patientList,
        // Person
        String dni,
        String first,
        String lastName,
        String age,
        String phoneNumber,

        LocalDate dateOfBirth,
        String bloodType,
        String documentEPS,
        String user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {}
