package com.example.ClinicaDefinitiva.domain.actor.output;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DentistRepository {
    Optional<Dentist> findById(DentistId id);
    Page<Dentist> findAll (Pageable pageable);
    Dentist save(Dentist dentist);
    Page<Dentist> findBySpecialty(String specialty, Pageable pageable);
    void deleteById(DentistId id);
    boolean existsById(Long id);

    Optional<Dentist> findByUserId(UserIdentityId id);
}
