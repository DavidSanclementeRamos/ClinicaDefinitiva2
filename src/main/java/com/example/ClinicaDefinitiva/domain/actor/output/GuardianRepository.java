package com.example.ClinicaDefinitiva.domain.actor.output;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GuardianRepository {
    Optional<Guardian> findById(GuardianId guardianId);
    Page<Guardian> findAll(Pageable pageable);
    void save(Guardian guardian);
    Guardian update(GuardianId guardianId);
    Guardian deleteById(GuardianId guardianId);
    Page<Guardian> findByPatientId(PatientId patientId, Pageable pageable);
    boolean existsById(GuardianId guardianId);

    Guardian findByUserId(UserIdentityId id);
}
