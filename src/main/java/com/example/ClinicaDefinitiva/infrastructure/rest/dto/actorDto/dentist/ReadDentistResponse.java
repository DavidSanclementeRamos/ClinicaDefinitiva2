package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de respuesta para Dentist
 */
public record ReadDentistResponse(
        Long dentistId,
        String specialties,
        String availabilityStatus,

        // WorkingHours de horas laborales
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
        String user,
        LocalDateTime lastUpdate,

        // Address
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {}





