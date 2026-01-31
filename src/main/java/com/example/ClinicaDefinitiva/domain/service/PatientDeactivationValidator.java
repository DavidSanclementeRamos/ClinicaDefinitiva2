package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.ScheduleRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;

public class PatientDeactivationValidator {
    private final ScheduleRepository scheduleRepository;
    private final int daysToBlockDeactivation;

    public PatientDeactivationValidator(ScheduleRepository scheduleRepository, int daysToBlockDeactivation) {

        this.scheduleRepository = scheduleRepository;
        this.daysToBlockDeactivation = daysToBlockDeactivation;
    }

    public Outcome<Void> validate(PatientId patientId) {
        Schedule schedule = scheduleRepository.findByPatientId(patientId);

        if (schedule != null && schedule.hasAppointmentsWithin(daysToBlockDeactivation)) {
            return Outcome.fail(new OutcomeDetail(
                    PatientError.ERR_PATIENT_ACTIVE_SERVICES,
                    Severity.INFO,
                    Category.CLINICO, EntityContext.PATIENT));
        }

        return Outcome.ok();
    }
}

