package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;
import java.time.Duration;
import java.time.LocalDateTime;


public class Appointment {

    private final AppointmentId id;
    private final DentistId dentist;
    private final PatientId patient;
    private LocalDateTime start;                 // fechaHora
    private LocalDateTime end;
    private AppointmentStatus status;                // estado
    private String reason;                           // motivo
    private AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
    private String clinicalNotes;                    // notas Clinicas (Professional observations)
    private ServiceDuration actualDuration;              // duracionReal (Efficiency analysis)
    private String attendedBy;                      // atendidaPor (May differ from assigned)
    private LocalDateTime creationDate;              // fecha creacion
    private LocalDateTime lastUpdated;               // ultima atualizacion
    private boolean rescheduled;
   // private ServiceDuration scheduledDuration;



    // Ventana mínima de cancelación: 2 horas antes de la cita
    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(24);

    public Appointment(ServiceDuration actualDuration, AppointmentType appointmentType, String attendedBy, String clinicalNotes, LocalDateTime creationDate, DentistId dentist, LocalDateTime end, AppointmentId id, LocalDateTime lastUpdated, PatientId patient, String reason, boolean rescheduled, LocalDateTime start, AppointmentStatus status) {
        this.actualDuration = actualDuration;
        this.appointmentType = appointmentType;
        this.attendedBy = attendedBy;
        this.clinicalNotes = clinicalNotes;
        this.creationDate = creationDate;
        this.dentist = dentist;
        this.end = end;
        this.id = id;
        this.lastUpdated = lastUpdated;
        this.patient = patient;
        this.reason = reason;
        this.rescheduled = rescheduled;
        this.start = start;
        this.status = status;
    }


    // ==================== OPERACIONES DE DOMINIO ====================

    /**
     * RN-APPT-006: Solo puede confirmarse si está en estado SCHEDULED
     */
    public void confirm() {
      if(!status.isScheduled()){
        throw new  BusinessRuleViolationException(AppointmentError.ERR_APPT_NOT_EDITABLE,EntityContext.APPOINTMENT);
      }
        this.status.transitionTo(AppointmentStatus.Status.CONFIRMED);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * RN-APPT-007: No puede cancelarse dentro de las 24h previas
     * RN-APPT-008: La cancelación requiere motivo obligatorio
     */
    public void cancel(String cancellationReason) {
        if (cancellationReason == null || cancellationReason.isBlank()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_MISSING_REASON,EntityContext.APPOINTMENT);
        }

        LocalDateTime now = LocalDateTime.now();
        if (start.minus(CANCELLATION_WINDOW).isBefore(now)) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_LATE_CANCELLATION,EntityContext.APPOINTMENT);
        }

        this.status = this.status.transitionTo(AppointmentStatus.Status.CANCELLED);
        this.clinicalNotes = (this.clinicalNotes == null ? "" : this.clinicalNotes + "\n")
                + "Cancelación: " + cancellationReason;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * RN-APPT-005: Solo puede finalizarse si tiene duración real y notas clínicas
     */
    public void complete(ServiceDuration actualDuration, String clinicalNotes, String attendedBy) {
        if (actualDuration == null || actualDuration.getMinutes() <= 0) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION,EntityContext.APPOINTMENT);
        }
        if (clinicalNotes == null || clinicalNotes.isBlank()) {
            throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION,EntityContext.APPOINTMENT);
        }

        this.status = this.status.transitionTo(AppointmentStatus.Status.COMPLETED);
        this.actualDuration = actualDuration;
        this.clinicalNotes = clinicalNotes;
        this.attendedBy = attendedBy;
        this.lastUpdated = LocalDateTime.now();
    }

    public void markAsNoShow(String notes) {
        this.status = this.status.transitionTo(AppointmentStatus.Status.NO_SHOW);
        this.clinicalNotes = "No Show: " + (notes != null ? notes : "Sin observaciones");
        this.lastUpdated = LocalDateTime.now();
    }

    public void markAsRescheduled() {
        this.status = this.status.transitionTo(AppointmentStatus.Status.RESCHEDULED);
        this.lastUpdated = LocalDateTime.now();
    }

    // ==================== QUERIES ====================

    public boolean esFutura() {
        return this.start.isAfter(LocalDateTime.now());
    }

    public boolean isWithinNext24Hours(LocalDateTime reference) {
        LocalDateTime limit = reference.plusHours(24);
        return start.isAfter(reference) && !start.isAfter(limit);
    }

    public boolean conflictsWith(LocalDateTime candidateStart, LocalDateTime candidateEnd) {
        if (start == null || end == null || candidateStart == null || candidateEnd == null) {
            return false;
        }
        return !(candidateEnd.isBefore(start) || candidateEnd.equals(start) ||
                candidateStart.isAfter(end) || candidateStart.equals(end));
    }

    // ==================== GETTERS ====================

    public AppointmentId getId() { return id; }
    public DentistId getDentistId() { return dentist; }
    public PatientId getPatientId() { return patient; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public AppointmentStatus getStatus() { return status; }
    public String getReason() { return reason; }
    public AppointmentType getAppointmentType() { return appointmentType; }
    public String getClinicalNotes() { return clinicalNotes; }
    public ServiceDuration getActualDuration() { return actualDuration; }
    public String getAttendedBy() { return attendedBy; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
