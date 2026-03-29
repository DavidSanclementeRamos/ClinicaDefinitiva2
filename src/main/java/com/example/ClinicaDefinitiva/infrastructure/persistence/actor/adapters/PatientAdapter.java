package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.GuardianJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.Patient.PatientReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.Patient.PatientWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ContractJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class PatientAdapter implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;
    private final UserIdentityJpaRepository userIdentityJpaRepository;
    private final GuardianJpaRepository guardianJpaRepository;
    private final ContractJpaRepository contractJpaRepository;
    private final PatientReadEntityMapper readMapper;
    private final PatientWriteEntityMapper writeMapper;

    public PatientAdapter(
            PatientJpaRepository patientJpaRepository,
            UserIdentityJpaRepository userIdentityJpaRepository,
            GuardianJpaRepository guardianJpaRepository,
            ContractJpaRepository contractJpaRepository,
            PatientReadEntityMapper readMapper,
            PatientWriteEntityMapper writeMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.userIdentityJpaRepository = userIdentityJpaRepository;
        this.guardianJpaRepository = guardianJpaRepository;
        this.contractJpaRepository = contractJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findById(PatientId id) {
        if (id == null || id.value() == null) {
            return Optional.empty();
        }
        return patientJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        return patientJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByContractId(ContractId contractId, Pageable pageable) {
        if (contractId == null || contractId.getValue() == null) {
            return Page.empty();
        }
        // Asumiendo que contractId.getValue() es Long, si es String ajustar según corresponda
        return patientJpaRepository.findByContractId(contractId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable) {
        if (guardianId == null || guardianId.value() == null) {
            return Page.empty();
        }
        return patientJpaRepository.findByGuardianId(guardianId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Patient save(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientEntity entity = writeMapper.toEntity(patient);
        
        // Establecer relaciones
        if (patient.getUser() != null && patient.getUser().value() != null) {
            userIdentityJpaRepository.findById(patient.getUser().value())
                    .ifPresent(entity::setUserIdentity);
        }
        
        if (patient.getGuardianId() != null && patient.getGuardianId().value() != null) {
            guardianJpaRepository.findById(patient.getGuardianId().value())
                    .ifPresent(entity::setGuardian);
        }
        
        if (patient.getContractId() != null && patient.getContractId().getValue() != null) {
            contractJpaRepository.findById(patient.getContractId().getValue())
                    .ifPresent(entity::setContract);
        }
        
        PatientEntity savedEntity = patientJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

   

    @Override
    public boolean existsById(PatientId id) {
        return id != null && id.value() != null && patientJpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(PatientId id) {
        if (id != null && id.value() != null) {
            patientJpaRepository.deleteById(id.value());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findByUserId(UserIdentityId userIdentityId) {
        if (userIdentityId == null || userIdentityId.value() == null) {
            return Optional.empty();
        }
        return patientJpaRepository.findByUserIdentityId(userIdentityId.value())
                .map(readMapper::toDomain);
    }
}