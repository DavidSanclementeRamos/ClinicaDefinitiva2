package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentMapper {
    public Appointment toDomain(Long dentistId, Long patientId, LocalDateTime start, LocalDateTime end) {
        return new Appointment(null, dentistId, patientId, start, end, AppointmentStatus.SCHEDULED);
    }
}
