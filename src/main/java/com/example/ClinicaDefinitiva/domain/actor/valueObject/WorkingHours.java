package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.CodigoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.NullWorkingHoursException;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.StartTimeAfterEndTimeException;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class WorkingHours {
    private final LocalTime start;
    private final LocalTime end;
    private final DayOfWeek dayOfWeek;

    public WorkingHours(LocalTime start, LocalTime end, DayOfWeek dayOfWeek) {
        if (start == null || end == null ||dayOfWeek == null) {
            throw new NullWorkingHoursException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
        }
        if( !start.isBefore(end) ){
            throw new StartTimeAfterEndTimeException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
        }
        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
    }
    public boolean isWithin(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.getDayOfWeek().equals(dayOfWeek)
                && !dateTime.toLocalTime().isBefore(start)
                && !dateTime.toLocalTime().isAfter(end);
    }

    public boolean cubre(TimeSlot slot) {
        if (slot == null) return false;
        if (!slot.getDayOfWeek().equals(this.dayOfWeek)) return false;
        return !slot.getInicio().isBefore(start) && !slot.getFin().isAfter(end);
    }

    public Duration duracionTotal() {
        return Duration.between(start, end);
    }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
}





