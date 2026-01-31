package com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.ReceptionId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReceptionRepository {
    Optional<Receptionist> findById(ReceptionId id);
    Page<Receptionist> findAll(Pageable pageable);
    void save(Receptionist receptionist);
    Receptionist update(Receptionist receptionist);
    boolean existsById(ReceptionId id);
    void deleteById(ReceptionId id);
    Page<Receptionist> findBySector(String sector, Pageable pageable);

    Receptionist findByUserId(UserId id);
}
