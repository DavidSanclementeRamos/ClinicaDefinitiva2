package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.stereotype.Service;

@Service
public class DentistDeactivationValidator {

    private final ScheduleQueryService scheduleQueryService;

    public DentistDeactivationValidator(ScheduleQueryService scheduleQueryService) {
        this.scheduleQueryService = scheduleQueryService;
    }

    public Outcome<Void> validate(DentistId dentistId) {
        if (scheduleQueryService.hasAppointmentsWithinHours(dentistId, 24)) {
            return Outcome.fail(new OutcomeDetail(
                    DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                    ErrorSeverity.INFO,
                    Category.CLINICO, EntityContext.DENTIST
            ));
        }
        return Outcome.ok();
    }
}