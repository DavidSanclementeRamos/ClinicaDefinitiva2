package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;

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

