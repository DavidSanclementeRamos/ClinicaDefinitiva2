package com.example.ClinicaDefinitiva.domain.administration.Operations;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class ShiftId   {
    private final String value;


    private ShiftId(String value) {
        this.value = Objects.requireNonNull(value,"El valor de ShiftId no puede ser nulo ");
    }

    public static ShiftId from(String value) {

        if (value == null) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_SHIFT_ID_REQUIRED, VOContext.SHIFT_ID);
        }
        if(value.isBlank()){
            throw new ValueObjectValidationException(ScheduleVOError.ERR_SHIFT_ID_BLANK,VOContext.SHIFT_ID);
        }
        return new ShiftId(value);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftId)) return false;
        ShiftId shiftId = (ShiftId) o;
        return Objects.equals(value, shiftId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
