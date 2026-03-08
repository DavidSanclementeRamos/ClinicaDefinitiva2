package com.example.ClinicaDefinitiva.application.dto.scheduled;


import java.time.LocalDateTime;

public record UpdateAppointmentDto (
    Long appointmentId,
    Long dentistId,
    Long patientId,
    LocalDateTime newStart,
    LocalDateTime newEnd

){}

