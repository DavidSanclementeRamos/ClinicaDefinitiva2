package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Schedule {
    // agenda de agregado de cista y disponibilidad
    private final List<Appointment> appointments;
    private final WeeklyAvailability weeklyAvailability;

    public Schedule(Collection<Appointment> appointments, WeeklyAvailability weeklyAvailability) {
        this.appointments = appointments == null ? new ArrayList<>() : new ArrayList<>(appointments);
        this.weeklyAvailability = weeklyAvailability == null ? new WeeklyAvailability(List.of()) : weeklyAvailability;
    }

    public List<Appointment> upcomingWithinHours(int hours) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
                .filter(Appointment::isScheduled)
                .filter(a -> !a.getDateTime().isBefore(now) && a.getDateTime().isBefore(now.plusHours(hours)))
                .collect(Collectors.toList());
    }

    public boolean hasAppointmentsWithinHours(int hours) {
        return !upcomingWithinHours(hours).isEmpty();
    }

    public boolean canScheduleAt(LocalDateTime dateTime) {
        // Validar contra disponibilidad declarada
        boolean coveredBySlot = weeklyAvailability.getSlots().stream()
                .anyMatch(slot -> slot.getDayOfWeek().equals(dateTime.getDayOfWeek())
                        && !dateTime.toLocalTime().isBefore(slot.getInicio())
                        && !dateTime.toLocalTime().isAfter(slot.getFin()));
        // además, verificar solapamiento con citas existentes
        boolean slotFree = appointments.stream()
                .filter(Appointment::isScheduled)
                .noneMatch(a -> a.conflictsWith(dateTime));
        return coveredBySlot && slotFree;
    }

    public List<Appointment> getAppointments() { return List.copyOf(appointments); }
    public WeeklyAvailability getWeeklyAvailability() { return weeklyAvailability; }



}
