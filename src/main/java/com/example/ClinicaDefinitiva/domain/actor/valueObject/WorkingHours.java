package com.example.ClinicaDefinitiva.domain.actor.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.Dentist.exception.NullWorkingHoursException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.Dentist.exception.StartTimeAfterEndTimeException;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class WorkingHours {
    private final LocalTime start;
    private final LocalTime end;
    private final DayOfWeek dayOfWeek;
    private final int declaredHoursPerWeek; //cumplimiento laboral declarado

    public WorkingHours(LocalTime start, LocalTime end, DayOfWeek dayOfWeek, int declaredHoursPerWeek) {
        if (start == null || end == null ||dayOfWeek == null) {
            throw new NullWorkingHoursException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
        }
        if( !start.isBefore(end) ){
            throw new StartTimeAfterEndTimeException(ContextoEntidad.WORKING_HOURS, "Invalid working hours.");
        }
        if (declaredHoursPerWeek <= 0) {
            throw new IllegalArgumentException("Las horas declaradas deben ser positivas");
        }


        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.declaredHoursPerWeek = declaredHoursPerWeek;
    }
    // verifica que dateTime cae dentro de un intervalo de tiempo específico en un día concreto de la semana
    public boolean isWithin(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.getDayOfWeek().equals(dayOfWeek)
                && !dateTime.toLocalTime().isBefore(start)
                && !dateTime.toLocalTime().isAfter(end);
    }

    // Nuevo: validar rango completo
    public boolean isWithinRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return false;

        return startDateTime.getDayOfWeek().equals(dayOfWeek)
                && endDateTime.getDayOfWeek().equals(dayOfWeek)
                && !startDateTime.toLocalTime().isBefore(start)
                && !endDateTime.toLocalTime().isAfter(end);
    }
    /** Se usa para validar reglas de cumplimiento:
      ¿el dentista está cumpliendo con lo que declaró como su jornada oficial?*/
    public boolean isCompliantWithWorkingHours(WeeklyAvailability availability) {
        return availability.totalHoras() >= this.declaredHoursPerWeek;
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





