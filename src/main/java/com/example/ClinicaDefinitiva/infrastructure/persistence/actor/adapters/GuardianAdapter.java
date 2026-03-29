package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.GuardianEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.GuardianJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.guardian.GuardianReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.guardian.GuardianWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class GuardianAdapter implements GuardianRepository {

    private final GuardianJpaRepository guardianJpaRepository;
    private final UserIdentityJpaRepository userIdentityJpaRepository;
    private final GuardianReadEntityMapper readEntityMapper;
    private final GuardianWriteEntityMapper writeEntityMapper;

    public GuardianAdapter(
            GuardianJpaRepository guardianJpaRepository,
            UserIdentityJpaRepository userIdentityJpaRepository,
            GuardianReadEntityMapper readEntityMapper,
            GuardianWriteEntityMapper writeEntityMapper) {
        this.guardianJpaRepository = guardianJpaRepository;
        this.userIdentityJpaRepository = userIdentityJpaRepository;
        this.readEntityMapper = readEntityMapper;
        this.writeEntityMapper = writeEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guardian> findById(GuardianId guardianId) {
        if (guardianId == null || guardianId.value() == null) {
            return Optional.empty();
        }
        return guardianJpaRepository.findById(guardianId.value())
                .map(readEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Guardian> findAll(Pageable pageable) {
        return guardianJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Guardian save(Guardian guardian) {
        if (guardian == null) {
            return null;
        }

        GuardianEntity entity = writeEntityMapper.toEntity(guardian);
        
        // Establecer relación con UserIdentity si existe
        if (guardian.getUserId() != null && guardian.getUserId().value() != null) {
            userIdentityJpaRepository.findById(guardian.getUserId().value())
                    .ifPresent(entity::setUserIdentity);
        }
        
        GuardianEntity savedEntity = guardianJpaRepository.save(entity);
        return readEntityMapper.toDomain(savedEntity);
    }

    

    @Override
    public void deleteById(GuardianId guardianId) {
         if (guardianId != null && guardianId.value() != null){
         guardianJpaRepository.deleteById(guardianId.value());
         }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Guardian> findByPatientId(PatientId patientId, Pageable pageable) {
        if (patientId == null || patientId.value() == null) {
            return Page.empty();
        }
        return guardianJpaRepository.findByPatientId(patientId.value(), pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(GuardianId guardianId) {
        return guardianId != null && guardianId.value() != null && 
               guardianJpaRepository.existsById(guardianId.value());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Guardian> findByUserId(UserIdentityId userIdentityId) {
        if (userIdentityId == null || userIdentityId.value() == null) {
            return Optional.empty();
        }
        return guardianJpaRepository.findByUserIdentityId(userIdentityId.value())
                .map(readEntityMapper::toDomain);
    }
}