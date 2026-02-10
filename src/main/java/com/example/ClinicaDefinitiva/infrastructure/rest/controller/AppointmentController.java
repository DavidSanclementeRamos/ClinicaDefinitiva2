package com.example.ClinicaDefinitiva.infrastructure.rest.controller;

import com.example.ClinicaDefinitiva.application.portsInput.AppointmentUseCase;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentUseCase appointmentUseCase;
    private final AppointmentMapper mapper;

    public AppointmentController(AppointmentUseCase appointmentUseCase, AppointmentMapper mapper) {
        this.appointmentUseCase = appointmentUseCase;
        this.mapper = mapper;
    }

    // Schedule new appointment
    @PostMapping
    public ResponseEntity<Appointment> schedule(
            @RequestParam("dentistId") @NotNull Long dentistId,
            @RequestParam("patientId") @NotNull Long patientId,
            @RequestParam("providedServiceId") @NotNull Long providedServiceId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        Appointment appointment = appointmentUseCase.schedule(dentistId, patientId, providedServiceId, start, end);
        return ResponseEntity.ok(appointment);
    }

    // Reschedule existing appointment
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<Appointment> reschedule(
            @PathVariable("id") Long appointmentId,
            @RequestParam("newStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newStart,
            @RequestParam("newEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEnd) {

        Appointment updated = appointmentUseCase.reschedule(appointmentId, newStart, newEnd);
        return ResponseEntity.ok(updated);
    }

    // Cancel appointment
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long appointmentId) {
        appointmentUseCase.cancel(appointmentId);
        return ResponseEntity.noContent().build();
    }
}


