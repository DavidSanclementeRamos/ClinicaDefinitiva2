package com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 * DTO para actualizar información sensible
 */
public record UpdateDentistSensitiveRequest(
        Long dentistId,
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
        String documentoEPS
        ){
}
