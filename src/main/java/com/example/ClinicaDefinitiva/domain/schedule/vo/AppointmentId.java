package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;


public record  AppointmentId(Long getValue) {

    public  static AppointmentId of(Long value) {
        if (value == null){
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPOINTMENT_ID_REQUIRED, VOContext.SCHEDULE);
        }
       return new AppointmentId(value);

    }

}
