package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;
import com.example.ClinicaDefinitiva.vo.EstadoCita;

import javax.xml.crypto.Data;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private Long id;
    private Dentist dentist;
    private Patient patient;
    private LocalDateTime dateTime;                 // fechaHora
    private LocalDateTime endDateTime;
    private AppointmentStatus status;                // estado
    private String reason;                           // motivo
    private AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
    private String clinicalNotes;                    // notas Clinicas (Professional observations)
    private LocalTime actualDuration;                 // duracionReal (Efficiency analysis)
    private Dentist attendedBy;                      // atendidaPor (May differ from assigned)
    private LocalDateTime creationDate;              // fecha creacion
    private LocalDateTime lastUpdated;               // ultima atualizacion
    private boolean rescheduled;

    // Ventana mínima de cancelación: 2 horas antes de la cita
    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(2);

    public Appointment(LocalTime actualDuration, AppointmentType appointmentType, Dentist attendedBy, String clinicalNotes, LocalDateTime creationDate, LocalDateTime dateTime, Dentist dentist, LocalDateTime endDateTime, Long id, LocalDateTime lastUpdated, Patient patient, String reason, boolean rescheduled, AppointmentStatus status) {
        this.actualDuration = actualDuration;
        this.appointmentType = appointmentType;
        this.attendedBy = attendedBy;
        this.clinicalNotes = clinicalNotes;
        this.creationDate = creationDate;
        this.dateTime = dateTime;
        this.dentist = dentist;
        this.endDateTime = endDateTime;
        this.id = id;
        this.lastUpdated = lastUpdated;
        this.patient = patient;
        this.reason = reason;
        this.rescheduled = rescheduled;
        this.status = status;
    }

    public boolean esFutura() {
        return this.dateTime.isAfter(LocalDateTime.now());
    }







       // private Appointment() { /* constructor privado */ }

        // 1) No puede agendarse si el odontólogo está inactivo
        // 2) No puede agendarse fuera del horario de disponibilidad del odontólogo
        // 4) Debe tener un paciente y un odontólogo válidos para ser confirmada

    public static Appointment schedule(Dentist dentist,
                                       Patient patient,
                                       LocalDateTime star,
                                       LocalDateTime end,
                                       AppointmentType type,
                                       String reason) {
        if (dentist == null ) {
            throw new IllegalArgumentException("Odontólogo inválido .");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Paciente inválido.");
        }
        if (!dentist.canScheduleAt(star)) {
            throw new IllegalArgumentException("Horario no disponible para el odontólogo.");
        }
        if (type == null || reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Tipo de cita y motivo son obligatorios.");
        }

        return new Appointment(
                null, // actualDuration
                type,
                null, // clinicalNotes
                null, // attendedBy
                LocalDateTime.now(), // creationDate
                star,
                end,
                dentist,
                LocalDateTime.now(), // lastUpdated
                patient,
                reason,
                false, // rescheduled
                AppointmentStatus.from(AppointmentStatus.Status.SCHEDULED)

                );
    }
        // 3) No puede eliminarse (cancelarse) si está a menos de X horas de su ejecución (2h)
        public void cancel() {
            LocalDateTime now = LocalDateTime.now();
            if (dateTime.minus(CANCELLATION_WINDOW).isBefore(now)) {
                throw new IllegalArgumentException(
                        "No se puede cancelar: faltan menos de "
                                + CANCELLATION_WINDOW.toHours()
                                + " horas para la cita");
            }
            this.status.isCancelled();
        }

        // Confirmar cita (puede usarse si necesitas un paso explícito de confirmación)
        public void confirm() {
            if (this.patient == null || this.dentist == null) {
                throw new IllegalArgumentException(
                        "No se puede confirmar: paciente u odontólogo inválido");
            }
            this.status.isConfirmed();
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
        return dateTime.isAfter(reference) && !dateTime.isAfter(limit);
    }

    public boolean conflictsWith(LocalDateTime candidate) {
        LocalDateTime start = this.getDateTime();
        LocalDateTime end = this.getEndDateTime(); // debe existir
        if (start == null || end == null || candidate == null) return false;
        // Consideramos conflicto si candidate cae dentro del intervalo [start, end)
        return !candidate.isBefore(start) && candidate.isBefore(end);


    }


    public Dentist getDentist() {
        return dentist;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Patient getPatient() {
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

    public Dentist getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(Dentist attendedBy) {
        this.attendedBy = attendedBy;
    }

    public LocalTime getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(LocalTime actualDuration) {
        this.actualDuration = actualDuration;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }


}
