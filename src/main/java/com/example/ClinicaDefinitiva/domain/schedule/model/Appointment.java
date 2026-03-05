package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentCompletion;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentType;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * Agregado: Appointment (Cita Médica)
 *
 * Constructor mediante Builder según ADR-07 (≥7 atributos obligatorios)
 */
public class Appointment {

    private final AppointmentId id;
    private final DentistId dentist;
    private final PatientId patient;
    private final ServiceId serviceId;
    private LocalDateTime start;
    private LocalDateTime end;
    private AppointmentStatus status;
    private String reason;
    private AppointmentType appointmentType;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdated;
    private AppointmentCompletion completion;

    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(24);


    private Appointment(Builder builder) {
        this.id = builder.id;
        this.dentist = builder.dentist;
        this.patient = builder.patient;
        this.serviceId = builder.serviceId;
        this.start = builder.start;
        this.end = builder.end;
        this.status = builder.status;
        this.reason = builder.reason;
        this.appointmentType = builder.appointmentType;
        this.creationDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.completion = builder.completion;
    }



    /**
     * RN-APPT-006: Solo puede confirmarse si está en estado SCHEDULED
     */
   /** public void confirm() {
        if (!status.isScheduled()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_NOT_EDITABLE, EntityContext.APPOINTMENT);
        }
        this.status = this.status.transitionTo(AppointmentStatus.Status.CONFIRMED);
        this.lastUpdated = LocalDateTime.now();
    }*/

    /**
     * RN-APPT-007: No puede cancelarse dentro de las 24h previas
     * RN-APPT-008: La cancelación requiere motivo obligatorio
     */
    public void cancel(String cancellationReason) {
        if (cancellationReason == null || cancellationReason.isBlank()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_MISSING_REASON, EntityContext.APPOINTMENT);
        }
        LocalDateTime now = LocalDateTime.now();
        if (start.minus(CANCELLATION_WINDOW).isBefore(now)) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_LATE_CANCELLATION, EntityContext.APPOINTMENT);
        }

        this.status = this.status.transitionTo(AppointmentStatus.Status.CANCELLED);
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * RN-APPT-005: Solo puede finalizarse si tiene duración real y notas clínicas
     */


    public void complete(AppointmentCompletion completion) {
        
        this.status = AppointmentStatus.from(AppointmentStatus.Status.COMPLETED);
        this.completion = completion;
        this.lastUpdated = LocalDateTime.now();

    }



    public void markAsNoShow(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleViolationException(
                    AppointmentError.ERR_APPT_MISSING_REASON, EntityContext.APPOINTMENT);
        }
        this.status = this.status.transitionTo(AppointmentStatus.Status.NO_SHOW);
        this.lastUpdated = LocalDateTime.now();
    }

    /**public void markAsRescheduled() {
        this.status = this.status.transitionTo(AppointmentStatus.Status.RESCHEDULED);
        this.lastUpdated = LocalDateTime.now();
    }*/



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



    public AppointmentId getId() { return id; }
    public DentistId getDentistId() { return dentist; }
    public PatientId getPatientId() { return patient; }
    public ServiceId getServiceId() { return serviceId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public AppointmentStatus getStatus() { return status; }
    public String getReason() { return reason; }
    public AppointmentType getAppointmentType() { return appointmentType; }
    public AppointmentCompletion getCompletion() { return completion; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }



    public static class Builder {
        private AppointmentId id;
        private DentistId dentist;
        private PatientId patient;
        private ServiceId serviceId;
        private LocalDateTime start;
        private LocalDateTime end;
        private AppointmentStatus status = AppointmentStatus.scheduled();
        private String reason;
        private AppointmentType appointmentType;
        private AppointmentCompletion completion;

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


        public Builder withServiceId(ServiceId serviceId) {
            this.serviceId = serviceId;
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

        public Builder withCompletion(AppointmentCompletion completion) {
            this.completion = completion;
            return this;
        }



        public Appointment build() {

      
            return new Appointment(this);
        }
    }
}
