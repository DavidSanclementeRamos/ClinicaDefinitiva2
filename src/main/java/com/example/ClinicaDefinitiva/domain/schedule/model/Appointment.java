package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;

//import java.time.ServiceDuration;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private AppointmentId id;
    private DentistId dentist;
    private PatientId patient;
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

    public Appointment (Builder b){
       // this.scheduledDuration = b.scheduledDuration;
        this.appointmentType = b.appointmentType;
        this.attendedBy = b.attendedBy;
        this.clinicalNotes = b.clinicalNotes;
        this.creationDate = b.creationDate;
        this.start = b.start;
        this.dentist = b.dentist;
        this.end = b.end;
        this.id = b.id;
        this.lastUpdated = b.lastUpdated;
        this.patient = b.patient;
        this.reason = b.reason;
        this.rescheduled = b.rescheduled;
        this.status = b.status;
    }

    public boolean esFutura() {
        return this.start.isAfter(LocalDateTime.now());
    }



    // Confirmar cita (puede usarse si necesitas un paso explícito de confirmación)
    public void confirm() {
        if (this.patient == null || this.dentist == null) {
            throw new IllegalArgumentException(
                    "No se puede confirmar: paciente u odontólogo inválido");
        }
        this.status.isConfirmed();
    }

    // 3) No puede eliminarse (cancelarse) si está a menos de X horas de su ejecución (2)
    public void cancel() {
        LocalDateTime now = LocalDateTime.now();
        if (start.minus(CANCELLATION_WINDOW).isBefore(now)) {
            throw new IllegalArgumentException(
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
      * */
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

    public static final class Builder{
        private AppointmentId id;
        private DentistId dentist;
        private PatientId patient;
        private LocalDateTime start;                 // fechaHora
        private LocalDateTime end;
        private AppointmentStatus status;                // estado
        private String reason;                           // motivo
        private AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
        private String clinicalNotes;                    // notas Clinicas (Professional observations)
        private ServiceDuration scheduledDuration;                 // duracionReal (Efficiency analysis)
        private String attendedBy;                      // atendidaPor (May differ from assigned)
        private LocalDateTime creationDate;              // fecha creacion
        private LocalDateTime lastUpdated;               // ultima atualizacion
        private boolean rescheduled;

        public  Builder withId(AppointmentId id){this.id = id; return this;}
        public Builder withDentistId(DentistId d){this.dentist = d; return this;}
        public Builder withPatientId(PatientId p){this.patient = p; return this;}
        public  Builder withStart(LocalDateTime s){this.start = s; return this;}
        public Builder withEnd(LocalDateTime e){this.end = e; return this;}
        public Builder withStatus(AppointmentStatus s){this.status = s; return this;}
        public Builder withReason(String r){this.reason = r; return this;}
        public Builder withAppointmentType(AppointmentType t){this.appointmentType = t; return this;}
        public Builder withClinicaNotes(String t){this.clinicalNotes = t; return this;}
        public Builder withServiceDuratio(ServiceDuration a){this.scheduledDuration = a; return this;}

        public Appointment buildAppointment(){
            return new Appointment(this);
        }
    }
}
