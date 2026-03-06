package com.example.ClinicaDefinitiva.domain.administration.operations.service;


import com.example.ClinicaDefinitiva.application.exceptions.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.operations.ShiftError;
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
                .orElseThrow(() -> new ProvidedServiceNotFoundException(""));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (!dentist.getWorkingHours().isWithinRange(startTime, endTime,dayOfWeek)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE, EntityContext.SHIFT
            );
        }

        Shift shift = Shift.create(
                dentistId,
                date,
                startTime,
                endTime,
                type
        );

        return shiftRepository.save(shift);
    }
}
