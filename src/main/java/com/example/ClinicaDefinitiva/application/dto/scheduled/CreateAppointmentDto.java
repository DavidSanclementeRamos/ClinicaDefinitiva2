package com.example.ClinicaDefinitiva.application.dto.sheduled;

import java.time.LocalDateTime;

public record CreateAppointmentDto (
     Long dentistId,
     Long patientId,
     Long serviceId,
     LocalDateTime start,
     LocalDateTime end ,
     String type ,
     String reason
){}

