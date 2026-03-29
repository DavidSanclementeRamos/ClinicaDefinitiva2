
package com.example.ClinicaDefinitiva.application.administration.operations.dto;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * DTO para asignar un turno.
 */
public record AssignShiftDto(
        Long dentistId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String type
) {}
