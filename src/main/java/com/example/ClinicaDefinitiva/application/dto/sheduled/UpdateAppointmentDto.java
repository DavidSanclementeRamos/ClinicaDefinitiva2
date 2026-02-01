package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

import java.time.LocalDateTime;

public class UpdateAppointmentDto {
    public final String appointmentId;
    public final String dentistId;
    public final String patientId;
    public final LocalDateTime newStart;
    public final LocalDateTime newEnd;
    public String providedServiceId;
    private final Schedule schedule;


    public UpdateAppointmentDto(Schedule schedule, String providedServiceId, String patientId, LocalDateTime newStart, LocalDateTime newEnd, String dentistId, String appointmentId) {
        this.schedule = schedule;
        this.providedServiceId = providedServiceId;
        this.patientId = patientId;
        this.newStart = newStart;
        this.newEnd = newEnd;
        this.dentistId = dentistId;
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getDentistId() {
        return dentistId;
    }

    public LocalDateTime getNewEnd() {
        return newEnd;
    }

    public LocalDateTime getNewStart() {
        return newStart;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProvidedServiceId() {
        return providedServiceId;
    }

    public void setProvidedServiceId(String providedServiceId) {
        this.providedServiceId = providedServiceId;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
