package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;

public class DentistAvailabilityService {
    public void changeAvailability(Dentist dentist, Schedule schedule , DentistAvailabilityStatus newStatus, int hoursRange) {
        if (schedule.hasActiveAppointmentsWithin(hoursRange)) {
            throw new BusinessRuleViolationException(
                    ErrorCatalogXD.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                    EntityContext.DENTIST,
                    "No puede cambiar estado con citas pendientes."
            );
        }
        dentist.changeAvailability(newStatus);
    }

}
