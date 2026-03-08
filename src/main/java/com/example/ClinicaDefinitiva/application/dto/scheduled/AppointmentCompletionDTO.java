package com.example.ClinicaDefinitiva.application.dto.sheduled;

public record AppointmentCompletionDTO(
        Long attendedBy,
        int actualDurationMinutes,
        String clinicalNotes
) {
}
