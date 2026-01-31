package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.DentistRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.util.Category;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.util.Severity;

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

        Dentist dentist = dentistRepo.findByUserId(user.getId());
        if (dentist != null) {
            result = result.merge(dentistValidator.validate(dentist.getDentistId()));
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient != null ) {
            result = result.merge( patientValidator.validate(patient.getPatientId()));
           result = result.merge(patient.validateDeactivation()
            );
        }

        Guardian guardian = guardianRepo.findByUserId(user.getId());
        if (guardian != null) {
            result = result.merge(guardian.validateDeactivation());
        }


        Receptionist receptionist = receptionistRepo.findByUserId(user.getId());
        if (receptionist != null ) {
            result = result.merge(receptionValidator.validate(receptionist.getId()));

        }

        return result;
    }
}
