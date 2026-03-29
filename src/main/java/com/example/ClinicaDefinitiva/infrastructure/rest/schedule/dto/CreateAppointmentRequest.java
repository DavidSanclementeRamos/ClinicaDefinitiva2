package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        Long dentistId,
        Long patientId,
        Long serviceId,
        LocalDateTime start,
        LocalDateTime end ,
        String type ,
        String reason
) {
}
