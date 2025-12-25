package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;

import java.time.LocalDateTime;

// Domain service puro, sin dependencias infra
public  class AppointmentDomainService {

    private AppointmentDomainService() { /* utilitarios estáticos */ }

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
                                               ProvidedService service,
                                               UserIdentity user) {
        if (dentist == null ) {
            throw new IllegalArgumentException("Odontólogo inválido .");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Paciente inválido.");
        }
        dentist.canScheduleBetween(user,star, end);

        patient.canScheduleBetween(user,star, end);

        if (type == null || reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Tipo de cita y motivo son obligatorios.");
        }
        if (scheduledDuration.getMinutes() != service.getDuration().getMinutes()) {
            throw new IllegalStateException("Appointment duration must match service duration");
        }

        return new Appointment.Builder()
                .withServiceDuratio(scheduledDuration)
                .withDentistId(dentist.getDentistId())
                .withAppointmentType(type)
                .withEnd(end)
                .withClinicaNotes("Experimental")
                .withPatientId(patient.getPatientId())
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

    public static Appointment validationReschedule(Appointment original,
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

        return new Appointment.Builder()
                .withStart(newStart)
                .withEnd(newEnd)
               // .withStatus(AppointmentStatus.from(AppointmentStatus.Status.)

                .buildAppointment();
    }

    public static Appointment cancelarCita(Appointment appointment) {

        if (! appointment.isScheduled()) {
            throw new IllegalArgumentException("La cita no está en estado programado");
        }
        if (appointment.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede cancelar una cita ya iniciada o pasada");
        }

        appointment.cancel(); // delegas en Appointment el cambio de estado

        return new Appointment.Builder()
                                .withStatus(AppointmentStatus.from(AppointmentStatus.Status.SCHEDULED)).buildAppointment();



    }


}