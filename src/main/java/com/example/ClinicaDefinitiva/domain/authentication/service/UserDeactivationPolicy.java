package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.service.DentistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.PatientDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.ReceptionistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;

import java.util.Optional;


public class UserDeactivationPolicy {
    private final DentistRepository dentistRepo;
    private final PatientRepository patientRepo;
    private final GuardianRepository guardianRepo;
    private final ReceptionRepository receptionistRepo;
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;
    private final ReceptionistDeactivationValidator receptionValidator;

    public UserDeactivationPolicy(DentistRepository dentistRepo, PatientRepository patientRepo, GuardianRepository guardianRepo, ReceptionRepository receptionistRepo, DentistDeactivationValidator dentistValidator, PatientDeactivationValidator patientValidator, ReceptionistDeactivationValidator receptionValidator) {
        this.dentistRepo = dentistRepo;
        this.patientRepo = patientRepo;
        this.guardianRepo = guardianRepo;
        this.receptionistRepo = receptionistRepo;
        this.dentistValidator = dentistValidator;
        this.patientValidator = patientValidator;
        this.receptionValidator = receptionValidator;
    }


    public Outcome<Void> validate(UserIdentity user) {

        Outcome<Void> result = Outcome.ok();

        Optional<Dentist> dentistOpt = dentistRepo.findByUserId(user.getId());
        if (dentistOpt.isPresent()) {
            result = result.merge(dentistValidator.validate(dentistOpt.get().getDentistId()));
        }

        Optional<Patient> patientOpt = patientRepo.findByUserId(user.getId());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            result = result.merge(patientValidator.validate(patient.getPatientId()));
            result = result.merge(patient.validateDeactivation());
        }

        Optional<Guardian> guardianOpt = guardianRepo.findByUserId(user.getId());
        if (guardianOpt.isPresent()) {
            result = result.merge(guardianOpt.get().validateDeactivation());
        }

        Optional<Receptionist> receptionistOpt = receptionistRepo.findByUserId(user.getId());
        if (receptionistOpt.isPresent()) {
            Receptionist receptionist = receptionistOpt.get();
            result = result.merge(receptionValidator.validate(receptionist.getId()));
        }


        return result;
    }
}
