package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.ReceptionistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;
import com.example.ClinicaDefinitiva.domain.util.Severity;

import java.util.List;

public class ReceptionistDeactivationValidator {
    private final ShiftRepository shiftRepository;

    public ReceptionistDeactivationValidator(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Outcome<Void> validate(ReceptionId receptionId) {
        List<Shift> shifts = shiftRepository.findByReceptionistId(receptionId);
        boolean hasActiveShifts = shifts != null && shifts.stream().anyMatch(Shift::isActive);
        if (hasActiveShifts) {
            return Outcome.fail(new OutcomeDetail(
                    ReceptionistError.ERR_RECEPTIONIST_ASSIGNED_SHIFTS,
                    Severity.INFO,
                    Category.CLINICO, EntityContext.RECEPTIONIST
            ));
        }
        return Outcome.ok();
    }
}

