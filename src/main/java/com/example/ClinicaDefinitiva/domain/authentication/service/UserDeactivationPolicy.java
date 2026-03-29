package com.example.ClinicaDefinitiva.domain.authentication.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.service.DentistDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.actor.service.PatientDeactivationValidator;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.util.Outcome;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserDeactivationPolicy {
    
    private final DentistRepository dentistRepo;
    private final PatientRepository patientRepo;
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;

    // Nota: Se eliminaron guardianRepo y receptionistRepo según ADR-14 y ADR-17

    public UserDeactivationPolicy(
            DentistRepository dentistRepo,
            PatientRepository patientRepo,
            DentistDeactivationValidator dentistValidator,
            PatientDeactivationValidator patientValidator) {
        this.dentistRepo = dentistRepo;
        this.patientRepo = patientRepo;
        this.dentistValidator = dentistValidator;
        this.patientValidator = patientValidator;
    }

    public Outcome<Void> validate(UserIdentity user) {
        Outcome<Void> result = Outcome.ok();

        // Validar si el usuario es dentista
        Optional<Dentist> dentistOpt = dentistRepo.findByUserId(user.getId());
        if (dentistOpt.isPresent()) {
            result = result.merge(dentistValidator.validate(dentistOpt.get().getDentistId()));
        }

        // Validar si el usuario es paciente
        Optional<Patient> patientOpt = patientRepo.findByUserId(user.getId());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            result = result.merge(patientValidator.validate(patient.getPatientId()));
            result = result.merge(patient.validateDeactivation()); // Validación interna del paciente
        }

        // Guardian tiene su propia validación interna en el agregado, no se necesita aquí
        // Receptionist no tiene validaciones según ADR-17

        return result;
    }
}