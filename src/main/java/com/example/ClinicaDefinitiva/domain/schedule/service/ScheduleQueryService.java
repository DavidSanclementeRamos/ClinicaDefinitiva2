package com.example.ClinicaDefinitiva.domain.schedule.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Query Service: Centraliza consultas complejas sobre la agenda
 *
 * Reemplaza a la clase Schedule (que no era un agregado legítimo).
 *
 * Responsabilidades:
 * - Proveer queries reutilizables sobre appointments
 * - Calcular métricas temporales (tiempo ocupado, disponible)
 * - Servir a múltiples consumidores (AppointmentSchedulingService, Dentist.deactivate, etc.)
 */
@Service
public class ScheduleQueryService {

    private final AppointmentRepository appointmentRepository;

    public ScheduleQueryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Encuentra citas programadas dentro de las próximas N horas
     *
     * Usado en: Dentist.deactivate() para validar RN-DENTIST-002
     */
    public List<Appointment> findAppointmentsWithinHours(DentistId dentistId, int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);

        return appointmentRepository.findByDentistBetween(dentistId, now, limit)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();
    }

    /**
     * Verifica si hay citas en las próximas N horas
     */
    public boolean hasAppointmentsWithinHours(DentistId dentistId, int hours) {
        return !findAppointmentsWithinHours(dentistId, hours).isEmpty();
    }

    /**
     * Encuentra todas las citas en una fecha específica
     */
    public List<Appointment> findAppointmentsOn(DentistId dentistId, LocalDate date) {
        return appointmentRepository.findByDentistAndDate(dentistId, date)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();
    }

    /**
     * Encuentra citas en los próximos N días
     */
    public List<Appointment> findAppointmentsWithin(DentistId dentistId, int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);

        return appointmentRepository.findByDentistBetween(dentistId, now, limit)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .toList();
    }

    /**
     * Verifica si hay citas en los próximos N días
     */
    public boolean hasAppointmentsWithin(DentistId dentistId, int days) {
        return !findAppointmentsWithin(dentistId, days).isEmpty();
    }

    /**
     * Calcula el tiempo total ocupado por citas activas en una fecha
     */
    public Duration getTotalOccupiedTime(DentistId dentistId, LocalDate date) {
        return findAppointmentsOn(dentistId, date)
                .stream()
                .map(a -> Duration.between(a.getStart(), a.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Cuenta cuántas citas activas hay en total
     */
    public int getActiveAppointmentCount(DentistId dentistId) {
        return (int) appointmentRepository.findByDentist(dentistId)
                .stream()
                .filter(a -> a.getStatus().isScheduled())
                .count();
    }

    /**
     * Verifica si un intervalo conflictúa con citas existentes
     */
    public boolean hasConflictingAppointments(
            DentistId dentistId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<Appointment> existing = appointmentRepository
                .findByDentistBetween(dentistId, start, end);

        return existing.stream()
                .filter(a -> a.getStatus().isScheduled())
                .anyMatch(a -> a.conflictsWith(start, end));
    }
}