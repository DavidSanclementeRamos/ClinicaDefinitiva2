package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DentistRepository {
    Optional<Dentist> findById(DentistId id);
    Page<Dentist> findAll (Pageable pageable);
    void save(Dentist dentist);
    Dentist update(DentistId id);
    Dentist deleteById(DentistId id);
}
