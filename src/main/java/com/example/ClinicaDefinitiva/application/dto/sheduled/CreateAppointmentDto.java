package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;

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

