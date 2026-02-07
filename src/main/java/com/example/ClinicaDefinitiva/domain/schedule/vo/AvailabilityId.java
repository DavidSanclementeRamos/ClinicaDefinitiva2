package com.example.ClinicaDefinitiva.domain.schedule.vo;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorSchedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class AvailabilityId   {

    private final String value;


    private AvailabilityId(String value) {
        this.value =   Objects.requireNonNull(value,"AvailabilityId cannot be null");

    }

    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static AvailabilityId fromString(String value) {
        if (value == null ){
            throw new ValueObjectValidationException(ScheduleVOError.ERR_AVAIL_ID_REQUIRED, VOContext.AVAILABILITY_ID);
        }

        if(value.isBlank()) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_AVAIL_ID_BLANK,VOContext.AVAILABILITY_ID);
        }
        return new AvailabilityId(value);
    }

    public String getValue() { return value; }

}
