package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;

//import java.time.ServiceDuration;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private Long id;
    private Dentist dentist;
    private Patient patient;
    private LocalDateTime start;                 // fechaHora
    private LocalDateTime end;
    private AppointmentStatus status;                // estado
    private String reason;                           // motivo
    private AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
    private String clinicalNotes;                    // notas Clinicas (Professional observations)
    //private LocalTime actualDuration;              // duracionReal (Efficiency analysis)
    private Dentist attendedBy;                      // atendidaPor (May differ from assigned)
    private LocalDateTime creationDate;              // fecha creacion
    private LocalDateTime lastUpdated;               // ultima atualizacion
    private boolean rescheduled;
    private ServiceDuration scheduledDuration;



    // Ventana mínima de cancelación: 2 horas antes de la cita
    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(2);

    public Appointment (Builder b){
        this.scheduledDuration = b.scheduledDuration;
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








    // 1) No puede agendarse si el odontólogo está inactivo
    // 2) No puede agendarse fuera del horario de disponibilidad del odontólogo
    // 4) Debe tener un paciente y un odontólogo válidos para ser confirmada.
    // 5) La duration de la cita debe coincidir con la del servicio.
    public static Appointment registerSchedule(Dentist dentist,
                                               Patient patient,
                                               LocalDateTime star,
                                               LocalDateTime end,
                                               AppointmentType type,
                                               String reason,
                                               ServiceDuration scheduledDuration,
                                               ProvidedService service) {
        if (dentist == null ) {
            throw new IllegalArgumentException("Odontólogo inválido .");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Paciente inválido.");
        }
        dentist.canScheduleBetween(star, end);

        patient.canScheduleBetween(star, end);

        if (type == null || reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Tipo de cita y motivo son obligatorios.");
        }
        if (scheduledDuration.getMinutes() != service.getDuration().getMinutes()) {
            throw new IllegalStateException("Appointment duration must match service duration");
        }



        return new Builder()
                .withServiceDuratio(scheduledDuration)
                .withDentist(dentist)
                .withAppointmentType(type)
                .withEnd(end)
                .withClinicaNotes("Experimental")
                .withPatient(patient)
                .withStart(star)
                .withStatus(AppointmentStatus.from(AppointmentStatus.Status.SCHEDULED))
                .withReason("Motivo")
                .buildAppointment();
    }

    // Una cita puede ser reagendada si:
    // paciente y odontólogo están activos
    // la nueva fecha de assignation no se solapa con otra cita
    // la nueva fecha está en el horario del odontólogo.
    // El paciente no tiene citas en esa nueva fecha
    // el odontólogo esta disponible en esa fecha
    // La cita está en estado programada
    // La cita existe
    // La cita es vigente, no ha pasado o ha iniciado
    //-----------------------------------------------------
    /**Cubres estado (solo citas programadas).
     • 	Cubres identidad (mismo paciente y odontólogo).
     • 	Cubres actividad (paciente y odontólogo activos).
     • 	Cubres agenda (slots disponibles y sin conflictos).
     • 	Cubres políticas de negocio (anticipación mínima y ventana máxima). */

    public static Appointment validateReschedule(Appointment original,
                                          LocalDateTime newStart,
                                          LocalDateTime newEnd,
                                          Schedule schedule,
                                          Patient patient,
                                          Dentist dentist) {

        // Validaciones de estado, asegurado que la cita este en estado Scheduled y no Cancelled o Completed.
        schedule.validateStatus(original);

        // Validaciones de identidad, asegura que la cita que se intenta reagendar
        // pertenezca al mismo paciente y odontólogo que está en contexto.
        schedule.validateIdentity(original,patient,dentist);

        // Validaciones de actividad
        dentist.validateReschedule(newStart, newEnd);
        patient.validateReschedule();

        // Validaciones de agenda
        schedule.validateScheduleBetween(newStart, newEnd);

        // Política de tiempo mínimo, no reagendar con menos de 24 h de anticipation
        final long MIN_HOURS_BEFORE_RESCHEDULE = 24;
        if (original.getStart().isBefore(LocalDateTime.now().plusHours(MIN_HOURS_BEFORE_RESCHEDULE))) {
            throw new IllegalArgumentException("No se puede reagendar con menos de "
                    + MIN_HOURS_BEFORE_RESCHEDULE + " horas de anticipación.");
        }

        // Ventana máxima, no reagendar mas alla de 6 meses.
        final long MAX_MONTHS_AHEAD = 6;
        if (newStart.isAfter(LocalDateTime.now().plusMonths(MAX_MONTHS_AHEAD))) {
            throw new IllegalArgumentException("No se puede reagendar más allá de "
                    + MAX_MONTHS_AHEAD + " meses en el futuro.");
        }

        // validation de disponibilidad neta
        if (schedule.getTotalAvailableTime(newStart.getDayOfWeek()).isZero()){
            throw new IllegalArgumentException("No hay disponibilidad en ese dia ");
        }

        return new Builder()
                .withPatient(patient)

                .buildAppointment();
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

    public void cancelarCita() {

        if (!this.isScheduled()) {
            throw new IllegalArgumentException("La cita no está en estado programado");
        }
        if (this.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede cancelar una cita ya iniciada o pasada");
        }

        this.cancel(); // delegas en Appointment el cambio de estado
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

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public void setDentist(Dentist dentist) {
        this.dentist = dentist;
    }

    public Dentist getDentist() {
        return dentist;
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

    public static final class Builder{
        private Long id;
        private Dentist dentist;
        private Patient patient;
        private LocalDateTime start;                 // fechaHora
        private LocalDateTime end;
        private AppointmentStatus status;                // estado
        private String reason;                           // motivo
        private AppointmentType appointmentType;         // tipo cita (Control, emergency, first-time)
        private String clinicalNotes;                    // notas Clinicas (Professional observations)
        private ServiceDuration scheduledDuration;                 // duracionReal (Efficiency analysis)
        private Dentist attendedBy;                      // atendidaPor (May differ from assigned)
        private LocalDateTime creationDate;              // fecha creacion
        private LocalDateTime lastUpdated;               // ultima atualizacion
        private boolean rescheduled;

        private Builder withId(Long id){this.id = id; return this;}
        private Builder withDentist(Dentist d){this.dentist = d; return this;}
        private Builder withPatient(Patient p){this.patient = p; return this;}
        private Builder withStart(LocalDateTime s){this.start = s; return this;}
        private Builder withEnd(LocalDateTime e){this.end = e; return this;}
        private Builder withStatus(AppointmentStatus s){this.status = s; return this;}
        private Builder withReason(String r){this.reason = r; return this;}
        private Builder withAppointmentType(AppointmentType t){this.appointmentType = t; return this;}
        private Builder withClinicaNotes(String t){this.clinicalNotes = t; return this;}
        private Builder withServiceDuratio(ServiceDuration a ){this.scheduledDuration = a; return this;}

        public Appointment buildAppointment(){
            return new Appointment(this);
        }
    }
}
