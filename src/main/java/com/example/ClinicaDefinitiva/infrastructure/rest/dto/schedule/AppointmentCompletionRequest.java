package com.example.ClinicaDefinitiva.infrastructure.rest.dto.schedule;

public record AppointmentCompletionRequest(Long attendedBy,
                                           int actualDurationMinutes,
                                           String clinicalNotes) {
}
