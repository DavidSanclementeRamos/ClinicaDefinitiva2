package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;

import java.util.Optional;

public interface DentistRepository {
    Optional<Dentist> findById(Long id);
}
