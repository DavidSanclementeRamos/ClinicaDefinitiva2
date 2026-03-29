package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class DentistVacationService {

    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;

    public DentistVacationService(AppointmentRepository appointmentRepository, DentistRepository dentistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
    }

    public void validateVacationRequest(DentistId dentistId, LocalDateTime start, LocalDateTime end) {
        if (!TimeIntervalRules.isValid(start, end)) {
            throw new BusinessRuleViolationException(
                    DentistError.ERR_DENTIST_INVALID_VACATION_RANGE, EntityContext.DENTIST
            );
        }

        Page<Appointment> conflicts = appointmentRepository.findByDentistBetween(dentistId, start, end, Pageable.unpaged());
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    DentistError.ERR_DENTIST_VACATION_CONFLICT, EntityContext.DENTIST
            );
        }

        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new DentistNotFoundException(""));

        dentist.applyVacation(start, end);
        dentistRepository.save(dentist);
    }
}

