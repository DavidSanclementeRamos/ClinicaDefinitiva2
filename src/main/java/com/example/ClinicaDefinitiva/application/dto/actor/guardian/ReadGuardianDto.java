package com.example.ClinicaDefinitiva.application.dto.actor.guardian;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.ReadPatientDto;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReadGuardianDto(
        Long guardianId,
        // TypeGuardian
        String code,
        String description,

        List<String> patientList,
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
