package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.PatientError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.schedule.service.ScheduleQueryService;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.ErrorSeverity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class PatientDeactivationValidator {

        private final ScheduleQueryService scheduleQueryService;

    private final int daysToBlockDeactivation;

    public PatientDeactivationValidator(ScheduleQueryService scheduleQueryService,
                        @Value("${clinic.patient.deactivation.days-to-block:7}") int daysToBlockDeactivation) {

        this.scheduleQueryService = scheduleQueryService;
        this.daysToBlockDeactivation = daysToBlockDeactivation;
    }

    

    public Outcome<Void> validate(PatientId patientId) {

        if ( scheduleQueryService.hasAppointmentsWithin(patientId,daysToBlockDeactivation)) {
            return Outcome.fail(new OutcomeDetail(
                    PatientError.ERR_PATIENT_ACTIVE_SERVICES,
                    ErrorSeverity.INFO,
                    Category.CLINICO, EntityContext.PATIENT));
        }

        return Outcome.ok();
    }
}

