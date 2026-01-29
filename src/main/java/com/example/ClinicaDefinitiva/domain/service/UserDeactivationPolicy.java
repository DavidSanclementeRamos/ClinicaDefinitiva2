package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.DentistRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;

public class UserDeactivationPolicy {
    private final DentistRepository dentistRepo;
    private final PatientRepository patientRepo;
    private final GuardianRepository guardianRepo;
    private final ReceptionRepository receptionistRepo;

    public UserDeactivationPolicy(
            DentistRepository dentistRepo,
            PatientRepository patientRepo,
            GuardianRepository guardianRepo,
            ReceptionRepository receptionistRepo
    ) {
        this.dentistRepo = dentistRepo;
        this.patientRepo = patientRepo;
        this.guardianRepo = guardianRepo;
        this.receptionistRepo = receptionistRepo;
    }

    public boolean canDeactivate(UserIdentity user) {
        Dentist dentist = dentistRepo.findByUserId(user.getId());
        if (dentist != null && dentist.hasAppointmentsPending()) {
            return false;
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient != null && patient.hasActiveTreatment()) {
            return false;
        }

        Guardian guardian = guardianRepo.findByUserId(user.getId());
        if (guardian != null && guardian.hasResponsibilitiesActive()) {
            return false;
        }

        Receptionist receptionist = receptionistRepo.findByUserId(user.getId());
        if (receptionist != null && receptionist.hasAssignedShifts()) {
            return false;
        }

        return true;
    }
}


