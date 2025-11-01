package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById(Long id);
}
