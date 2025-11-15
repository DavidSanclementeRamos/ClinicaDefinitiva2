package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.application.dto.ServiceRendered;
import com.example.ClinicaDefinitiva.application.dto.ServiceRenderedDto;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;

import java.time.LocalDateTime;

public class CreateAppointmentDto {
    public final String dentistId;
    public final String patientId;
    public final LocalDateTime start;
    public final LocalDateTime end;
    public final AppointmentType type;
    public final ServiceDuration serviceDuration;
    public final String providedServiceId;
    public final String reason;

    public CreateAppointmentDto(String dentistId, String patientId, LocalDateTime start,
                                LocalDateTime end, AppointmentType type, ServiceDuration serviceDuration ,String providedServiceId, String reason) {
        this.dentistId = dentistId; this.patientId = patientId; this.start = start;
        this.end = end; this.type = type; this.providedServiceId = providedServiceId; this.reason = reason;
        this.serviceDuration = serviceDuration;
    }

    public String getDentistId() {
        return dentistId;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProvidedServiceId() {
        return providedServiceId;
    }

    public String getReason() {
        return reason;
    }

    public ServiceDuration getServiceDuration() {
        return serviceDuration;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public AppointmentType getType() {
        return type;
    }
}
