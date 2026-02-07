package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.service.UserAccessValidator;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;

import java.time.Instant;
import java.time.LocalDateTime;

public class PatientCanScheduleBetween {
    private final UserAccessValidator userAccessValidator;
    private final PatientRepository patientRepository;

    public PatientCanScheduleBetween(UserAccessValidator userAccessValidator, PatientRepository patientRepository) {
        this.userAccessValidator = userAccessValidator;
        this.patientRepository = patientRepository;
    }

    public void  canScheduleBetween (UserIdentity user, LocalDateTime start, LocalDateTime end ){
        Patient patient = patientRepository.findByUserId(user.getId());
        userAccessValidator.validateUserCanPerformSensitiveAction(user.getId(), Instant.now(), EntityContext.DENTIST);
        //user.canPerformSensitiveAction(Instant.now());

        patient.
    }
}
