package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;
import java.time.Duration;
import java.time.LocalDateTime;


public class Appointment {

    private final  AppointmentId id;
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
    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(2);

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

    public boolean esFutura() {
        return this.start.isAfter(LocalDateTime.now());
    }



    // Confirmar cita (puede usarse si necesitas un paso explícito de confirmación)
    public void confirm() {
        if (this.patient == null ) {
            throw new BusinessRuleViolationException(
                    "No se puede confirmar: paciente inválido");
        }
        if(this.dentist == null) {
            throw new BusinessRuleViolationException(
                    "No se puede confirmar: odontólogo inválido");
        }

        this.status.isConfirmed();
    }

    // 3) No puede eliminarse (cancelarse) si está a menos de X horas de su ejecución (2)
    public void cancel() {
        LocalDateTime now = LocalDateTime.now();
        if (start.minus(CANCELLATION_WINDOW).isBefore(now)) {
            throw new BusinessRuleViolationException(
                    "No se puede cancelar: faltan menos de "
                            + CANCELLATION_WINDOW.toHours()
                            + " horas para la cita");
        }
        this.status.isCancelled();
    }



     /**
      Se decidió delegar la lógica de estado desde Appointment hacia su VO AppointmentStatus,
      manteniendo la semántica encapsulada pero accesible. Esto permite que entidades como Dentist
      consulten el estado sin acoplarse a la estructura interna del VO.
      */
    public boolean isScheduled() {
        return this.status.isScheduled();
    }

    public boolean isWithinNext24Hours(LocalDateTime reference) {
        LocalDateTime limit = reference.plusHours(24);
        return start.isAfter(reference) && !start.isAfter(limit);
    }

    public boolean conflictsWith(LocalDateTime candidate) {
        LocalDateTime start = this.start;
        LocalDateTime end = this.end; // debe existir
        if (start == null || end == null || candidate == null) return false;
        // Consideramos conflicto si candidate cae dentro del intervalo [start, end)
        return !candidate.isBefore(start) && candidate.isBefore(end);


    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public void setRescheduled(boolean rescheduled) {
        this.rescheduled = rescheduled;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }


    public DentistId getDentistId() {
        return dentist;
    }


    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public PatientId getPatientId() {
        return patient;
    }

    public boolean isRescheduled() {
        return rescheduled;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }



    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ServiceDuration getActualDuration() {
        return actualDuration;
    }

    public DentistId getDentist() {
        return dentist;
    }

    public AppointmentId getId() {
        return id;
    }

    public PatientId getPatient() {
        return patient;
    }

    public String getReason() {
        return reason;
    }


    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }


}
