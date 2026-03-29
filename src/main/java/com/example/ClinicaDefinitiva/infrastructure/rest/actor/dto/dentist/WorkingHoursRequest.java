
package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.dentist;

import java.time.DayOfWeek;
import java.time.LocalTime;


public record WorkingHoursRequest(
        // WorkingHours de horas laborales
        LocalTime start,
        LocalTime end,
        DayOfWeek dayOfWeek,
        int declaredHoursPerWeek) {
    
    
}
