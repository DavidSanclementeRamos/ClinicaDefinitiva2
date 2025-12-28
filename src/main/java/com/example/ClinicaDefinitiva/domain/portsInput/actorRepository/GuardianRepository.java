package com.example.ClinicaDefinitiva.domain.portsInput.actorRepository;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GuardianRepository {
    Optional<Guardian> findById(GuardianId guardianId);
    Page<Guardian> findAll(Pageable pageable);
    void save(Guardian guardian);
    Guardian update(GuardianId guardianId);
    Guardian desactive(GuardianId guardianId);

}
