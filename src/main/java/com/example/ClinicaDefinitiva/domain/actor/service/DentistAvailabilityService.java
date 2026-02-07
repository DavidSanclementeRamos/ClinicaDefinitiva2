package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;

public class DentistAvailabilityService {
    public void changeAvailability(Dentist dentist, ScheduleQueryService schedule , DentistAvailabilityStatus newStatus, int hoursRange) {
        if (schedule.hasAppointmentsWithinHours(hoursRange)) { // hasActiveAppointmentsWithin
            throw new BusinessRuleViolationException(
                    DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                    EntityContext.DENTIST

            );
        }
        dentist.changeAvailability(newStatus);
    }

}
