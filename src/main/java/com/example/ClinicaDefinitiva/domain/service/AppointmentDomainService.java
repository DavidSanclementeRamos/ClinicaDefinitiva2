package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentStatus;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentType;

import java.time.Duration;
import java.time.LocalDateTime;

// Domain service puro, sin dependencias infra
public final class AppointmentDomainService {

    private static final Duration CANCELLATION_WINDOW = Duration.ofHours(2);
    private static final long MIN_HOURS_FOR_RESCHEDULE = 24;
    private static final long MAX_MONTHS_AHEAD = 6;

    private AppointmentDomainService() { /* utilitarios estáticos */ }

    // Validaciones previas a crear la cita localmente (no persiste ni reserva)
    public  Appointment registerSchedule(Dentist dentist,
                                           Patient patient,
                                           LocalDateTime start,
                                           LocalDateTime end,
                                           ServiceDuration scheduledDuration,
                                           ProvidedService providedService,
                                           AppointmentType type,
                                           String reason) {


        if (dentist == null ) {
            throw new IllegalArgumentException("Odontólogo inválido .");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Paciente inválido.");
        }
        // Delega en los agregados las validaciones de actividad y disponibilidad
        dentist.canScheduleBetween(start, end);
        patient.canScheduleBetween(start, end);

        // Validación de duración usando servicio provisto
        if (scheduledDuration.getMinutes() != providedService.getDuration().getMinutes()) {
            throw new BusinessException("La duración de la cita no coincide con la del servicio");
        }

        // cualquier política adicional que sea pura y no requiera infra puede ir aquí
        Appointment cita = Appointment.registerSchedule(
                dentist,
                patient,
                start,
                end,
                type,
                reason,
                scheduledDuration,
                providedService

                );
        return cita;
    }

    // Validaciones puras para reagendar (no reserva slots ni persiste)
    public static void UpdateReschedule(Appointment original,
                                             LocalDateTime newStart,
                                             LocalDateTime newEnd,
                                             Dentist dentist,
                                             Patient patient) {
        // estado y coherencia local (delegado por quien llama si es necesario)
        if (original == null) throw new IllegalArgumentException("Cita original inexistente");

        // El agregado original sabe su estado; orquestador puede haber verificado que está Scheduled.
        // Validaciones de identidad/actividad delegadas a los agregados
        dentist.validateReschedule(newStart, newEnd);
        patient.validateReschedule();

        // Política pura: anticipación mínima y ventana máxima
        if (original.getStart().isBefore(LocalDateTime.now().plusHours(MIN_HOURS_FOR_RESCHEDULE))) {
            throw new BusinessException("No se puede reagendar con menos de " + MIN_HOURS_FOR_RESCHEDULE + " horas");
        }
        if (newStart.isAfter(LocalDateTime.now().plusMonths(MAX_MONTHS_AHEAD))) {
            throw new BusinessException("No se puede reagendar más allá de " + MAX_MONTHS_AHEAD + " meses");
        }

        // Nota: no comprobamos solapamiento con otras citas ni disponibilidad global aquí
    }

    // Validaciones puras para cancelar
    public static void validateCanCancel(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException("Cita inexistente");
        if (!appointment.isScheduled()) {
            throw new BusinessException("Solo citas en estado programado pueden cancelarse");
        }
        // Política: ventana mínima de cancelación (pura)
        if (appointment.getStart().minus(CANCELLATION_WINDOW).isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se puede cancelar: ventana de cancelación vencida");
        }
        // No se consulta dentist/ patient aquí porque sus agregados ya implementan ensureActive en validaciones previas
    }
}