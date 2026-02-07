package com.example.ClinicaDefinitiva.application.dto.sheduled;

import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;

import java.time.LocalDateTime;

public class ReadAppointmentDto {
    private  String appointmentId;
    private  String dentist;
    private  String patient;
    private  LocalDateTime start;                 // fechaHora
    private  LocalDateTime end;
    private  AppointmentStatus status;                // estado
    private  String reason;                           // motivo
    private  AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
    private  String clinicalNotes;                    // notas Clinicas (Professional observations)
    private ServiceDuration actualDuration;              // duracionReal (Efficiency analysis)
    private  String attendedBy;                      // atendidaPor (May differ from assigned)
    private  LocalDateTime creationDate;              // fecha creacion
    private  LocalDateTime lastUpdated;               // ultima atualizacion
    private  boolean rescheduled;

    public ReadAppointmentDto(ServiceDuration actualDuration, String appointmentId, AppointmentType appointmentType, String attendedBy, String clinicalNotes, LocalDateTime creationDate, String dentist, LocalDateTime end, LocalDateTime lastUpdated, String patient, String reason, boolean rescheduled, LocalDateTime start, AppointmentStatus status) {
        this.actualDuration = actualDuration;
        this.appointmentId = appointmentId;
        this.appointmentType = appointmentType;
        this.attendedBy = attendedBy;
        this.clinicalNotes = clinicalNotes;
        this.creationDate = creationDate;
        this.dentist = dentist;
        this.end = end;
        this.lastUpdated = lastUpdated;
        this.patient = patient;
        this.reason = reason;
        this.rescheduled = rescheduled;
        this.start = start;
        this.status = status;
    }

    public ReadAppointmentDto() {

    }

    public void setActualDuration(ServiceDuration actualDuration) {
        this.actualDuration = actualDuration;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setDentist(String dentist) {
        this.dentist = dentist;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRescheduled(boolean rescheduled) {
        this.rescheduled = rescheduled;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public ServiceDuration getActualDuration() {
        return actualDuration;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public String getAttendedBy() {
        return attendedBy;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getDentist() {
        return dentist;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getPatient() {
        return patient;
    }

    public String getReason() {
        return reason;
    }

    public boolean isRescheduled() {
        return rescheduled;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}
