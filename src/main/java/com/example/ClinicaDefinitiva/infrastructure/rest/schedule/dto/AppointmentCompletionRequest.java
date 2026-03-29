package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto;

public record AppointmentCompletionRequest(Long attendedBy,
                                           int actualDurationMinutes,
                                           String clinicalNotes) {
}
