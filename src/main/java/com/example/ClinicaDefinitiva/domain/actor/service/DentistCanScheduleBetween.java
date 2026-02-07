package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.domain.authentication.UserRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;

import java.time.Instant;
import java.time.LocalDateTime;

public class DentistCanScheduleBetween {
    private final DentistRepository dentistRepository;
    private final UserAccessValidator  userAccessValidator;
    public DentistCanScheduleBetween(DentistRepository dentistRepository, UserAccessValidator userAccessValidator) {
        this.dentistRepository = dentistRepository;
        this.userAccessValidator = userAccessValidator;
    }

    public void  canScheduleBetween (UserIdentity user,LocalDateTime start, LocalDateTime end ){
       Dentist dentist =  dentistRepository.findByUserId(user.getId());
        userAccessValidator.validateUserCanPerformSensitiveAction(user.getId(),Instant.now(),EntityContext.DENTIST);
        dentist.canScheduleBetween(start,end);

    }
}
