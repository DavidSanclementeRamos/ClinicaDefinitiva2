package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.application.exceptions.*;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.ReceptionId;
import com.example.ClinicaDefinitiva.domain.portsInput.*;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.util.Actor;
import com.example.ClinicaDefinitiva.domain.util.Outcome;

import java.util.Optional;

public class DesactivarActorService {

    private final UserRepository userRepositoryRepo;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final GuardianRepository guardianRepository;
    private final ReceptionRepository receptionRepository;

    public DesactivarActorService(UserRepository userRepositoryRepo, PatientRepository patientRepository, DentistRepository dentistRepository, GuardianRepository guardianRepository, ReceptionRepository receptionRepository) {
        this.userRepositoryRepo = userRepositoryRepo;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.guardianRepository = guardianRepository;
        this.receptionRepository = receptionRepository;
    }


    public Outcome desactivarPaciente(UserId userId, PatientId patientId) {
        Optional<UserIdentity> userOptional = userRepositoryRepo.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserIdentityNofoundException("");
        }
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if(patientOptional.isEmpty()){
            throw new PatientNotFoundException("");
        }
        Patient patient = patientOptional.get();
        UserIdentity user = userOptional.get();
        Outcome outcome = user.desactivarActor(patient);
        if (!outcome.isSuccess()) {
            return outcome;
        }

        patientRepository.save(patient);
        return Outcome.ok();
    }

    public Outcome desactivarOdontologo(UserId userId, DentistId dentistId) {

        Optional<UserIdentity> userOptional = userRepositoryRepo.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserIdentityNofoundException("");
        }

        Optional<Dentist> dentistOptional = dentistRepository.findById(dentistId);
        if(dentistOptional.isEmpty()){
            throw new DentistNotFoundException("");
        }
        UserIdentity user = userOptional.get();
        Dentist dentist = dentistOptional.get();

        Outcome outcome = user.desactivarActor((Actor) dentist);
        if (!outcome.isSuccess()) {
            return outcome;
        }

        dentistRepository.save(dentist);
        return Outcome.ok();
    }

    public Outcome desactivarResponsable(UserId userId, GuardianId guardianId) {
        Optional<UserIdentity> userOptional = userRepositoryRepo.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserIdentityNofoundException("");
        }

        Optional<Guardian> guardianOptional = guardianRepository.findById(guardianId);
        if(guardianOptional.isEmpty()){
            throw new GuardianNoFoundException("");
        }

        UserIdentity user = userOptional.get();
        Guardian guardian = guardianOptional.get();

        Outcome outcome = user.desactivarActor((Actor) guardian);
        if (!outcome.isSuccess()) {
            return outcome;
        }

        guardianRepository.save(guardian);
        return Outcome.ok();
    }

    public Outcome desactivarSecretario(UserId userId, ReceptionId receptionId) {
        Optional<UserIdentity> userOptional = userRepositoryRepo.findById(userId);
        if (userOptional.isEmpty()){
            throw new UserIdentityNofoundException("");
        }
        Optional<Receptionist> receptionOptional = receptionRepository.findById(receptionId);
        if(receptionOptional.isEmpty()){
            throw new ReceptionNotFoundException("");
        }
        Receptionist receptionist = receptionOptional.get();
        UserIdentity user = userOptional.get();


        Outcome outcome = user.desactivarActor(receptionist);
        if (!outcome.isSuccess()) {
            return outcome;
        }

        receptionRepository.save(receptionist);
        return Outcome.ok();
    }
}