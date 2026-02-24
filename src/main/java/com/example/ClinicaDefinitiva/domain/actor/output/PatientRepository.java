package com.example.ClinicaDefinitiva.domain.actor.output;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById( PatientId id);
    Page<Patient> findAll(Pageable pageable);
    Page<Patient> findByContractId (ContractId contractId, Pageable pageable);
    Page<Patient>findByGuardianId( GuardianId guardianId, Pageable pageable);
    Patient save(Patient patient);
    Patient update(PatientId id);
    boolean existsById( PatientId id);
    void deleteById( PatientId id);

    Optional<Patient> findByUserId(UserIdentityId id);
}
