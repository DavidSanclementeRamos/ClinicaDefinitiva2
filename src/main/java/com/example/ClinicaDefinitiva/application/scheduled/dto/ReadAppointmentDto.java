package com.example.ClinicaDefinitiva.application.schedule.dto;

import java.time.LocalDateTime;

public record ReadAppointmentDto (

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
){}

