package com.example.ClinicaDefinitiva.domain.actor.output;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
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

    Optional<Receptionist> findByUserId(UserId id);
}
