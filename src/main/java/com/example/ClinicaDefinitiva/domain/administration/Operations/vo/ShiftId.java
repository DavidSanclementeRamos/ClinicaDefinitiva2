package com.example.ClinicaDefinitiva.domain.administration.Operations.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class ShiftId   {
    private final Long value;


    private ShiftId(Long value) {
        this.value = Objects.requireNonNull(value,"El valor de ShiftId no puede ser nulo ");
    }

    public static ShiftId from(Long value) {

        if (value == null) {
            throw new ValueObjectValidationException(OperationsVoError.ERR_SHIFT_ID_REQUIRED, VOContext.OPERATIONS);
        }

        return new ShiftId(value) ;
    }

    public Long getValue() { return value; }

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
