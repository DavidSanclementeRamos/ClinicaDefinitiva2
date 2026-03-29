package com.example.ClinicaDefinitiva.application.schedule.dto;


import java.time.LocalDateTime;

public record UpdateAppointmentDto (
    Long appointmentId,
    Long dentistId,
    Long patientId,
    LocalDateTime newStart,
    LocalDateTime newEnd

){}

