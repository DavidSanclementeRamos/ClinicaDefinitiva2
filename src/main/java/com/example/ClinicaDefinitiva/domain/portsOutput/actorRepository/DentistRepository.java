package com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DentistRepository {
    Optional<Dentist> findById(DentistId id);
    Page<Dentist> findAll (Pageable pageable);
    Dentist save(Dentist dentist);
   // Dentist updateSensitiveData(Dentist dentist);
   // Dentist updateContactData(Dentist dentist);
    Page<Dentist> findByAvailability(String status, Pageable pageable);
    Page<Dentist> findBySpecialty(String specialty, Pageable pageable);
    void deleteById(DentistId id);
    boolean existsById(Long id);
}
