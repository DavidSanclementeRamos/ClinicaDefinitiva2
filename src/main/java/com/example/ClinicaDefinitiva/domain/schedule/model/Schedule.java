package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Schedule: Agregador que coordina Appointments y WeeklyAvailability
 * ✅ DOMINIO PURO - Sin dependencias de infraestructura
 *
 * Propósito: Proveer queries y cálculos sobre la agenda completa
 * NO valida conflictos (eso es responsabilidad del Domain Service con locks)
 */
public final class Schedule {

    private final List<Appointment> appointments;
    private final Availability availability;

    public Schedule(Collection<Appointment> appointments, Availability availability) {
        this.appointments = appointments == null ? new ArrayList<>() : new ArrayList<>(appointments);
        this.availability = availability;
    }

    // ==================== QUERIES SEMÁNTICAS ====================

    /**
     * Encuentra citas programadas dentro de las próximas N horas
     * Útil para: Políticas de cancelación, notificaciones, validaciones de desactivación
     */
    public List<Appointment> findAppointmentsWithinHours(int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusHours(hours);

        return appointments.stream()
                .filter(a -> a.getStatus().isScheduled()) // SCHEDULED o CONFIRMED
                .filter(a -> a.getStart().isAfter(now) && a.getStart().isBefore(limit))
                .toList();
    }

    /**
     * Encuentra todas las citas en una fecha específica
     * Útil para: Reportes diarios, validaciones de capacidad
     */
    public List<Appointment> findAppointmentsOn(LocalDate date) {
        return appointments.stream()
                .filter(a -> a.getStatus().isScheduled())
                .filter(a -> a.getStart().toLocalDate().equals(date))
                .toList();
    }

    /**
     * Encuentra citas en los próximos N días
     * Útil para: Políticas de ventana máxima, reportes semanales
     */
    public List<Appointment> findAppointmentsWithin(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit = now.plusDays(days);

        return appointments.stream()
                .filter(a -> a.getStatus().isScheduled())
                .filter(a -> a.getStart().isAfter(now) && a.getStart().isBefore(limit))
                .toList();
    }

    /**
     * Verifica si hay citas en las próximas N horas
     * Útil para: Validación rápida en Dentist.deactivate()
     */
    public boolean hasAppointmentsWithinHours(int hours) {
        return !findAppointmentsWithinHours(hours).isEmpty();
    }

    /**
     * Verifica si hay citas en los próximos N días
     */
    public boolean hasAppointmentsWithin(int days) {
        return !findAppointmentsWithin(days).isEmpty();
    }

    // ==================== CÁLCULOS TEMPORALES ====================

    /**
     * Calcula el tiempo total ocupado por citas activas
     * Útil para: Métricas de ocupación, reportes de productividad
     */
    public Duration getTotalOccupiedTime() {
        return appointments.stream()
                .filter(a -> a.getStatus().isScheduled())
                .map(a -> Duration.between(a.getStart(), a.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Calcula el tiempo disponible neto en un día específico
     * Útil para: Validar si hay capacidad antes de reagendar
     */
    public Duration getTotalAvailableTime(DayOfWeek day) {
        // Obtener slots del día
        List<TimeSlot> slotsForDay = Availability.getSlots().stream()
                .filter(slot -> slot.getDayOfWeek().equals(day))
                .toList();

        // Calcular tiempo total disponible declarado
        Duration totalSlots = slotsForDay.stream()
                .map(slot -> Duration.between(slot.getStartTime(), slot.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);

        // Calcular tiempo ocupado en ese día
        Duration occupied = appointments.stream()
                .filter(a -> a.getStatus().isScheduled())
                .filter(a -> a.getStart().getDayOfWeek().equals(day))
                .map(a -> Duration.between(a.getStart(), a.getEnd()))
                .reduce(Duration.ZERO, Duration::plus);

        // Tiempo neto disponible
        Duration available = totalSlots.minus(occupied);
        return available.isNegative() ? Duration.ZERO : available;
    }

    /**
     * Cuenta cuántas citas activas hay en total
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(a -> a.getStatus().isScheduled())
                .count();
    }

    /**
     * Verifica si la agenda está completamente vacía
     */
    public boolean isEmpty() {
        return appointments.isEmpty();
    }

    /**
     * Verifica si hay disponibilidad neta en un día
     */
    public boolean hasAvailabilityOn(DayOfWeek day) {
        return !getTotalAvailableTime(day).isZero();
    }

    // ==================== GETTERS INMUTABLES ====================

    public List<Appointment> getAppointments() {
        return List.copyOf(appointments);
    }

    public Availability getWeeklyAvailability() {
        return availability;
    }
}