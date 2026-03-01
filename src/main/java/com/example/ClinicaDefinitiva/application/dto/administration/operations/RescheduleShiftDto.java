
package com.example.ClinicaDefinitiva.application.dto.administration.operations;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * DTO para reagendar un turno.
 */
public record RescheduleShiftDto(
        LocalDate newDate,
        LocalTime newStart,
        LocalTime newEnd,
        boolean hasAuthorization
) {}
