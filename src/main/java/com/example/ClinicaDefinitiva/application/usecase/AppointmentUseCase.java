package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;

import java.time.LocalDateTime;

public interface AppointmentUseCase {
    Appointment save(Long dentistId, Long patientId, Long providedServiceId, LocalDateTime start, LocalDateTime end);
    Appointment update(Long appointmentId, LocalDateTime newStart, LocalDateTime newEnd);
    void daleById(Long appointmentId);

}
