package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.AppointmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class AppointmentCompletion {
    private final ServiceDuration actualDuration;
    private final String clinicalNotes;

    public AppointmentCompletion(ServiceDuration actualDuration, String clinicalNotes) {
        if (actualDuration == null || actualDuration.getMinutes() <= 0) {
            throw new ValueObjectValidationException(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION, VOContext.COMPLETION);
        }
        if (clinicalNotes == null || clinicalNotes.isBlank()) {
            throw new ValueObjectValidationException(AppointmentError.ERR_APPT_INCOMPLETE_COMPLETION, VOContext.COMPLETION);
        }
        this.actualDuration = actualDuration;
        this.clinicalNotes = clinicalNotes;
    }

    public ServiceDuration getActualDuration() { return actualDuration; }
    public String getClinicalNotes() { return clinicalNotes; }
}

