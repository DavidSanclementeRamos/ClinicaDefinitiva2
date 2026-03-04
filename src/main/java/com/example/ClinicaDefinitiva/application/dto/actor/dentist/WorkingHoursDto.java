
package com.example.ClinicaDefinitiva.application.dto.actor.dentist;

import java.time.DayOfWeek;
import java.time.LocalTime;


public record WorkingHoursDto(
        // WorkingHours de horas laborales
        LocalTime start,
        LocalTime end,
        DayOfWeek dayOfWeek,
        int declaredHoursPerWeek) {
    
}
