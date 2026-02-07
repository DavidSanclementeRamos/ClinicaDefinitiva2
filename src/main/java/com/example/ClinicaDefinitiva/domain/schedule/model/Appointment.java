package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
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



    // Ventana mínima de cancelación: 24 horas antes de la cita
    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(24);


    private Appointment(Builder builder) {
        this.id = builder.id;
        this.dentist = builder.dentist;
        this.patient = builder.patient;
        this.start = builder.start;
        this.end = builder.end;
        this.status = builder.status;
        this.reason = builder.reason;
        this.appointmentType = builder.appointmentType;
        this.clinicalNotes = builder.clinicalNotes;
        this.actualDuration = builder.actualDuration;
        this.attendedBy = builder.attendedBy;
        this.creationDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }


    // OPERACIONES DE DOMINIO

    /**
     * RN-APPT-006: Solo puede confirmarse si está en estado SCHEDULED
     */
    public void confirm() {
      if(!status.isScheduled()){
        throw new BusinessRuleViolationException(AppointmentError.ERR_APPT_NOT_EDITABLE,EntityContext.APPOINTMENT);
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

    // QUERIES

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

    // GETTERS

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


    // BUILDER

    public static class Builder {
        private AppointmentId id;
        private DentistId dentist;
        private PatientId patient;
        private LocalDateTime start;
        private LocalDateTime end;
        private AppointmentStatus status = AppointmentStatus.scheduled();
        private String reason;
        private AppointmentType appointmentType;
        private String clinicalNotes;
        private ServiceDuration actualDuration;
        private String attendedBy;

        public Builder withId(AppointmentId id) {
            this.id = id;
            return this;
        }

        public Builder withDentistId(DentistId dentist) {
            this.dentist = dentist;
            return this;
        }

        public Builder withPatientId(PatientId patient) {
            this.patient = patient;
            return this;
        }

        public Builder withStart(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public Builder withEnd(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public Builder withStatus(AppointmentStatus status) {
            this.status = status;
            return this;
        }

        public Builder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder withAppointmentType(AppointmentType type) {
            this.appointmentType = type;
            return this;
        }

        public Builder withClinicalNotes(String notes) {
            this.clinicalNotes = notes;
            return this;
        }

        public Builder withServiceDuration(ServiceDuration duration) {
            this.actualDuration = duration;
            return this;
        }

        public Builder withAttendedBy(String attendedBy) {
            this.attendedBy = attendedBy;
            return this;
        }

        public Appointment build() {
            if (id == null) throw new IllegalArgumentException("AppointmentId is required");
            if (dentist == null) throw new IllegalArgumentException("DentistId is required");
            if (patient == null) throw new IllegalArgumentException("PatientId is required");
            if (start == null) throw new IllegalArgumentException("Start time is required");
            if (end == null) throw new IllegalArgumentException("End time is required");
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("Reason is required");
            }
            if (appointmentType == null) {
                throw new IllegalArgumentException("AppointmentType is required");
            }
            if (!start.isBefore(end)) {
                throw new IllegalArgumentException("Start must be before end");
            }

            return new Appointment(this);
        }
    }
}

