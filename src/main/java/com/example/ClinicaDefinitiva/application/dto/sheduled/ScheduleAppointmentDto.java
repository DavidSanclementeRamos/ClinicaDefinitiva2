package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;

import java.time.LocalDateTime;

public class ScheduleAppointmentDto {
    public final String dentistId;
    public final String patientId;
    public final LocalDateTime start;
    public final LocalDateTime end;
    public final AppointmentType type;
    public final String providedServiceId;
    public final String reason;

    public ScheduleAppointmentDto(String dentistId, String patientId, LocalDateTime start,
                                      LocalDateTime end, AppointmentType type, String providedServiceId, String reason) {
        this.dentistId = dentistId; this.patientId = patientId; this.start = start;
        this.end = end; this.type = type; this.providedServiceId = providedServiceId; this.reason = reason;
    }


}
