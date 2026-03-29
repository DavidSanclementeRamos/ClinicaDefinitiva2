package com.example.ClinicaDefinitiva.application.schedule.dto;

public record AppointmentCompletionDTO(
        Long attendedBy,
        int actualDurationMinutes,
        String clinicalNotes
) {
}
