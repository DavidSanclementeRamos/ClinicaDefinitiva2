
package com.example.ClinicaDefinitiva.application.administration.operations.dto;

import java.time.LocalDateTime;


/**
 * DTO para verificar si un turno puede acomodar una cita.
 */
public record CanAccommodateAppointmentDto(
        LocalDateTime appointmentStart,
        LocalDateTime appointmentEnd
) {}
