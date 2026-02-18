package com.example.ClinicaDefinitiva.domain.administration.Operations.service;


import com.example.ClinicaDefinitiva.application.exceptions.DentalServiceNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.Operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.Operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.Operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.operations.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftAssignmentService {
    private final DentistRepository dentistRepository;
    private final ShiftRepository shiftRepository;

    public ShiftAssignmentService(DentistRepository dentistRepository, ShiftRepository shiftRepository) {
        this.dentistRepository = dentistRepository;
        this.shiftRepository = shiftRepository;
    }

    public Shift assignShift(
            DentistId dentistId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            ShiftType type) {

        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new DentalServiceNotFoundException(""));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (!dentist.getWorkingHours().isWithinRange(startTime, endTime,dayOfWeek)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE, EntityContext.SHIFT
            );
        }

        Shift shift = Shift.create(
                null,
                dentistId,
                date,
                startTime,
                endTime,
                type
        );

        return shiftRepository.save(shift);
    }
}
