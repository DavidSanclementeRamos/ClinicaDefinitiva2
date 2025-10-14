package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules.overlaps;

public class Shift {
// turno

    private DayOfWeek dayOfWeek;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Appointment> appointments;
    private boolean active;



    // No puede modificarse el horario si ya existen citas en ese rango
    public void reschedule(LocalDateTime newStart, LocalDateTime newEnd) {

        if(newStart == null || newEnd == null || !newStart.isBefore(newEnd)){
            throw new IllegalArgumentException("Rango de turno invalido ");
        }

        List<Appointment> conflicts = appointments.stream()
                .filter(a -> TimeIntervalRules.overlaps(
                        a.getStart(), a.getEnd(), newStart, newEnd))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot reschedule: " + conflicts.size() + " appointment"
                            + (conflicts.size() > 1 ? "s" : "") + " scheduled in the new range"
            );
        }

        this.start = newStart;
        this.end = newEnd;
    }

   // private DayOfWeek dayOfWeek;
   // private LocalTime start;
    //private LocalTime end;
    //private boolean active;*/

    public boolean isAvailableAt(LocalDateTime dateTime) {
        if (dateTime == null || !active) return false;
        return dateTime.getDayOfWeek().equals(dayOfWeek)
                && !dateTime.toLocalTime().isBefore(LocalTime.from(start))
                && !dateTime.toLocalTime().isAfter(LocalTime.from(end));
    }

    // Nuevo: validar rango completo
    public boolean isAvailableBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null || !active) return false;

        return startDateTime.getDayOfWeek().equals(dayOfWeek)
                && endDateTime.getDayOfWeek().equals(dayOfWeek)
                && !startDateTime.toLocalTime().isBefore(LocalTime.from(start))
                && !endDateTime.toLocalTime().isAfter(LocalTime.from(end));
    }
}




