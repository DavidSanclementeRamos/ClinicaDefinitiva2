
package com.example.ClinicaDefinitiva.application.dto.administration.operations;

import java.time.LocalDateTime;


/**
 * DTO para verificar si un turno puede acomodar una cita.
 */
public record CanAccommodateAppointmentDto(
        LocalDateTime appointmentStart,
        LocalDateTime appointmentEnd
) {}
