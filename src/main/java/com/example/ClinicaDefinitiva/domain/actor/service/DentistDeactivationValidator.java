package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;


public class DentistDeactivationValidator {
    private final ScheduleRepository scheduleRepository;

    public DentistDeactivationValidator(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Outcome<Void> validate(DentistId dentistId) {
        ScheduleQueryService schedule = scheduleRepository.findByDentistId(dentistId);

        if (schedule.hasAppointmentsWithinHours(dentistId,24)) {
            return Outcome.fail(new OutcomeDetail(
                    DentistError.ERR_DENTIST_ACTIVE_APPOINTMENTS,
                    Severity.INFO,
                    Category.CLINICO, EntityContext.DENTIST
            ));
        }

        return Outcome.ok();
    }
}
