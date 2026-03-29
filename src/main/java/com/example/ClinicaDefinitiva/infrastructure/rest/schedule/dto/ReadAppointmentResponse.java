package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto;

import java.time.LocalDateTime;

public record ReadAppointmentResponse(
        Long appointmentId,
        Long dentistId,
        Long patientId,
        Long ServiceId,
        LocalDateTime start,
        LocalDateTime end,
        String status,
        String reason,
        String appointmentType,
        String clinicalNotes,
        String actualDuration,
        LocalDateTime creationDate,
        LocalDateTime lastUpdated
) {
}
