package com.example.ClinicaDefinitiva.application.dto.actor.dentist;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 * DTO para actualizar información sensible
 */
public record UpdateDentistSensitiveDto(
       // Long dentistId,
        String specialties,
        // WorkingHours de horas laborales
        LocalTime start,
        LocalTime end,
        DayOfWeek dayOfWeek,
        int declaredHoursPerWeek,

        // Person
        String dni,
        String first,
        String lastName,
        String age, LocalDate dateOfBirth,
        String bloodType,
        String documentEPS
) {
}
