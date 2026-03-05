package com.example.ClinicaDefinitiva.domain.schedule.vo;

import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceDuration;
import com.example.ClinicaDefinitiva.domain.errors.catalog.schedule.ScheduleVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class AppointmentCompletion {
    private final ServiceDuration actualDuration;
    private final String clinicalNotes;

    public AppointmentCompletion(ServiceDuration actualDuration, String clinicalNotes) {
        if (actualDuration == null || actualDuration.getMinutes() <= 0) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPT_INCOMPLETE_COMPLETION, VOContext.SCHEDULE);
        }
        if (clinicalNotes == null || clinicalNotes.isBlank()) {
            throw new ValueObjectValidationException(ScheduleVOError.ERR_APPT_INCOMPLETE_COMPLETION, VOContext.SCHEDULE);
        }
        this.actualDuration = actualDuration;
        this.clinicalNotes = clinicalNotes;
    }

    public ServiceDuration getActualDuration() { return actualDuration; }
    public String getClinicalNotes() { return clinicalNotes; }
}

