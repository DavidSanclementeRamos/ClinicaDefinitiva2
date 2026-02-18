package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class WorkingHours {
    private final LocalTime start;
    private final LocalTime end;
    private final DayOfWeek dayOfWeek;
    private final int declaredHoursPerWeek; //cumplimiento laboral declarado

    private WorkingHours(LocalTime start, LocalTime end, DayOfWeek dayOfWeek, int declaredHoursPerWeek) {
        // Validación de nulidad
        if (start == null || end == null || dayOfWeek == null) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_WORKING_HOURS_NULL, VOContext.ACTORS);
        }

        // Validación de orden temporal
        if (!start.isBefore(end)) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_WORKING_HOURS_INVALID_RANGE, VOContext.ACTORS);
        }

        // Validación de horas declaradas
        if (declaredHoursPerWeek <= 0) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_WORKING_HOURS_INVALID_DECLARED,
                    VOContext.ACTORS);
        }

        // Validación de horas máximas (jornada laboral legal)
        if (declaredHoursPerWeek > 48) {
            throw new ValueObjectValidationException(
                    VoActorError.ERR_WORKING_HOURS_EXCEEDS_LEGAL_LIMIT, VOContext.ACTORS);
        }

        this.start = start;
        this.end = end;
        this.dayOfWeek = dayOfWeek;
        this.declaredHoursPerWeek = declaredHoursPerWeek;
    }
    public static WorkingHours of (LocalTime start, LocalTime end, DayOfWeek dayOfWeek, int declaredHoursPerWeek){
        return new WorkingHours(start,end,dayOfWeek,declaredHoursPerWeek);
    }

    // verifica que dateTime cae dentro de un intervalo de tiempo específico en un día concreto de la semana
    public boolean isWithin(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.getDayOfWeek().equals(dayOfWeek)
                && !dateTime.toLocalTime().isBefore(start)
                && !dateTime.toLocalTime().isAfter(end);
    }

    // Nuevo: validar rango completo
    public boolean isWithinRange(LocalTime startDateTime, LocalTime endDateTime,DayOfWeek day ) {
        if (startDateTime == null || endDateTime == null) return false;

        return day.equals(dayOfWeek)
                && !startDateTime.isBefore(start)
                && !endDateTime.isAfter(end);
    }
    /** Se usa para validar reglas de cumplimiento:
      ¿el dentista está cumpliendo con lo que declaró como su jornada oficial?
    public boolean isCompliantWithWorkingHours(WeeklyAvailability availability) {
        return availability.totalHoras() >= this.declaredHoursPerWeek;
    }*/


    public Duration duracionTotal() {
        return Duration.between(start, end);
    }

    public int getDeclaredHoursPerWeek() {
        return declaredHoursPerWeek;
    }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }


}





