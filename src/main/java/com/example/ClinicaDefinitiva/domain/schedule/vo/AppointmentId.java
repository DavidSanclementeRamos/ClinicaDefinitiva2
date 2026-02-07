package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class AppointmentId {
    private final String value;

    public AppointmentId(String value) {
        this.value = Objects.requireNonNull(value, "AppointmentId value cannot be null");
    }



    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static AppointmentId fromString(String value) {
        if (value == null) throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_ID_REQUIRED, VOContext.APPOINTMENT_ID);
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_ID_EMPTY,VOContext.APPOINTMENT_ID);
        return new AppointmentId(trimmed);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppointmentId)) return false;
        AppointmentId that = (AppointmentId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }


}
