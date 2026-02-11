package com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule;

import java.time.LocalDateTime;

public record UpdateAppointmentRequest(
        Long appointmentId,
        Long dentistId,
        Long patientId,
        LocalDateTime newStart,
        LocalDateTime newEnd
) {
}
