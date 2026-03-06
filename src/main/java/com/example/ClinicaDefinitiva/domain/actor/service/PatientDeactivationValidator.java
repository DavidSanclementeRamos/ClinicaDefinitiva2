package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;

public class PatientDeactivationValidator {
    private final ScheduleRepository scheduleRepository;
    private final int daysToBlockDeactivation;

    public PatientDeactivationValidator(ScheduleRepository scheduleRepository, int daysToBlockDeactivation) {

        this.scheduleRepository = scheduleRepository;
        this.daysToBlockDeactivation = daysToBlockDeactivation;
    }

    public Outcome<Void> validate(PatientId patientId) {
        ScheduleQueryService schedule = scheduleRepository.findByPatientId(patientId);

        if (schedule != null && schedule.hasAppointmentsWithin(patientId,daysToBlockDeactivation)) {
            return Outcome.fail(new OutcomeDetail(
                    PatientError.ERR_PATIENT_ACTIVE_SERVICES,
                    ErrorSeverity.INFO,
                    Category.CLINICO, EntityContext.PATIENT));
        }

        return Outcome.ok();
    }
}

