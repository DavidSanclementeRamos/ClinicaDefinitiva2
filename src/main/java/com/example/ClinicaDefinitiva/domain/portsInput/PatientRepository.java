package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById( PatientId id);
    Page<Patient> findAll(Pageable pageable);
    void save(Patient patient);
    Patient update(PatientId id);

}
