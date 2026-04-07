package com.example.ClinicaDefinitiva.domain.administration.operations.service;

import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.operations.ShiftError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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

        // ✅ Validar que la fecha no sea pasada
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_DATE_IN_PAST, EntityContext.SHIFT
            );
        }

        //  Buscar dentista con excepción correcta
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new DentistNotFoundException("Dentist not found with id: " + dentistId));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        //  Validar que el horario esté dentro de las horas laborales del dentista
        if (!dentist.getWorkingHours().isWithinRange(startTime, endTime, dayOfWeek)) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_NO_ACTIVE_COVERAGE, EntityContext.SHIFT
            );
        }

        //  Validar que no exista otro turno solapado
        boolean overlappingExists = shiftRepository.findOverlapping(
                dentistId, date, startTime, endTime, false, Pageable.unpaged()
        ).hasContent();

        if (overlappingExists) {
            throw new BusinessRuleViolationException(
                    ShiftError.ERR_SHIFT_OVERLAP, EntityContext.SHIFT
            );
        }

        Shift shift = Shift.create(dentistId, date, startTime, endTime, type);
        return shiftRepository.save(shift);
    }
}