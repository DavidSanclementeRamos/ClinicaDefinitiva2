package com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById( PatientId id);
    Page<Patient> findAll(Pageable pageable);
    Page<Patient> findByContractId (ContractId contractId, Pageable pageable);
    Page<Patient>findByGuardianId( GuardianId guardianId, Pageable pageable);
    void save(Patient patient);
    Patient update(PatientId id);
    boolean existsById( PatientId id);
    void deleteById( PatientId id);

    Patient findByUserId(UserId id);
}
