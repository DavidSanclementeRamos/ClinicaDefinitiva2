
package com.example.ClinicaDefinitiva.application.administration.operations.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para representar un turno en listados paginados.
 * Contiene solo la información esencial para mostrar en tablas o grids.
 */
public record PageShiftDto(
        Long id,
        Long dentistId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String type,
        String status
) {}

