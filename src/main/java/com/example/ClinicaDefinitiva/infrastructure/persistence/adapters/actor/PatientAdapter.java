package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.PatientRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.PatientEntityMapper.PatientReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.PatientEntityMapper.PatientWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientAdapter implements PatientRepository {

    private final PatientWriteEntityMapper writeMapper;
    private final PatientReadEntityMapper readMapper;
    private final PatientJpaRepository jpaRepository;

    public PatientAdapter(PatientWriteEntityMapper writeMapper, PatientReadEntityMapper readMapper, PatientJpaRepository jpaRepository) {
        this.writeMapper = writeMapper;
        this.readMapper = readMapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Patient> findById(PatientId id) {
        if (id == null) return Optional.empty();
        try {
            Long value = Long.valueOf(id.getValue());
            return jpaRepository.findById(value).map(writeMapper::toDomain);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Patient> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(writeMapper::toDomain);
    }

    @Override
    public Page<Patient> findByContractId(ContractId contractId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Patient> findByContractId(ContractId contractId) {
        if (contractId == null) return Optional.empty();
        String val = contractId.asLong() != null ? String.valueOf(contractId.asLong()) : contractId.toString();
        List<Patient> found = jpaRepository.findAll().stream()
                .filter(e -> e.getContractId() != null && e.getContractId().equals(val))
                .map(writeMapper::toDomain)
                .collect(Collectors.toList());
        return found.stream().findFirst();
    }

    @Override
    public Optional<Patient> findByGuardianId(GuardianId guardianId) {
        if (guardianId == null) return Optional.empty();
        Long val = guardianId.getValue();
        List<Patient> found = jpaRepository.findAll().stream()
                .filter(e -> e.getGuardian() != null && e.getGuardian().getGuardianId() == val)
                .map(writeMapper::toDomain)
                .collect(Collectors.toList());
        return found.stream().findFirst();
    }

    @Override
    public void save(Patient patient) {
        if (patient == null) return;
        var entity = readMapper.toEntity(patient);
        jpaRepository.save(entity);
    }

    @Override
    public Patient update(PatientId id) {
        if (id == null) return null;
        try {
            Long val = Long.valueOf(id.getValue());
            var entity = jpaRepository.findById(val).orElse(null);
            if (entity == null) return null;
            // mapping back to domain
            return writeMapper.toDomain(jpaRepository.save(entity));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean existsById(PatientId id) {
        if (id == null) return false;
        try {
            return jpaRepository.existsById(Long.valueOf(id.getValue()));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteById(PatientId id) {
        if (id == null) return;
        try {
            jpaRepository.deleteById(Long.valueOf(id.getValue()));
        } catch (Exception ignored) {
        }
    }
}
