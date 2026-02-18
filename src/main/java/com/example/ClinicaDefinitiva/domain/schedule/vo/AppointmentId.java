package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.util.Objects;

public final class AppointmentId {
    private final Long value;

    public AppointmentId(Long value) {
        if (value == null){
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_ID_REQUIRED, VOContext.SCHEDULE);
        }

        this.value = value ;
    }



    // Nuevo: parsea/valida una cadena y devuelve el VO
    public static AppointmentId of(Long value) {
       return new AppointmentId(value);

    }

    public Long getValue() { return value; }





}
