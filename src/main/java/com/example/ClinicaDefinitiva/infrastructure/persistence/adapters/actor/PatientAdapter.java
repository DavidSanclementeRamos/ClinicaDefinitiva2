package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.GuardianJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.Patient.PatientReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.Patient.PatientWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PatientAdapter implements PatientRepository {

    private final PatientJpaRepository jpaRepository;
    private final GuardianJpaRepository guardianJpaRepository;
    private final PatientReadEntityMapper readMapper;
    private final PatientWriteEntityMapper writeMapper;

    public PatientAdapter(
            PatientJpaRepository jpaRepository,
            GuardianJpaRepository guardianJpaRepository,
            PatientReadEntityMapper readMapper,
            PatientWriteEntityMapper writeMapper) {
        this.jpaRepository = jpaRepository;
        this.guardianJpaRepository = guardianJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findById(PatientId id) {
        return jpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByContractId(ContractId contractId, Pageable pageable) {
        String contractIdStr = contractId.getValue().toString();
        return jpaRepository.findByContractId(contractIdStr, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable) {
        return jpaRepository.findByGuardianId(guardianId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional
    public Patient save(Patient patient) {
        PatientEntity entity = writeMapper.toEntity(patient);
        
        // Asociar guardian si existe
        if (patient.getGuardianId() != null) {
            guardianJpaRepository.findById(patient.getGuardianId().value())
                    .ifPresent(entity::setGuardian);
        }
        
        PatientEntity savedEntity = jpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Patient update(PatientId id) {
        // Este método parece incorrecto en la interfaz, debería recibir Patient
        // Por ahora lo dejamos como está pero no se usará
        return findById(id).orElse(null);
    }

    // Método adicional útil (no en interfaz pero lo agregamos)
    @Transactional
    public Patient update(Patient patient) {
        if (patient == null || patient.getPatientId() == null) {
            return null;
        }
        
        PatientEntity entity = writeMapper.toEntity(patient);
        
        if (patient.getGuardianId() != null) {
            guardianJpaRepository.findById(patient.getGuardianId().value())
                    .ifPresent(entity::setGuardian);
        }
        
        PatientEntity savedEntity = jpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(PatientId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    @Transactional
    public void deleteById(PatientId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findByUserId(UserIdentityId id) {
        return jpaRepository.findByUserId(id.value().toString())
                .map(readMapper::toDomain);
    }
}