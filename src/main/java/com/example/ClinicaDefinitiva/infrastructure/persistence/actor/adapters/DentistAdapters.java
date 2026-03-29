package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.dentist.DentistReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.dentist.DentistWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class DentistAdapters implements DentistRepository {

    private final DentistJpaRepository dentistJpaRepository;
    private final UserIdentityJpaRepository userIdentityJpaRepository;
    private final DentistWriteEntityMapper writeEntityMapper;
    private final DentistReadEntityMapper readEntityMapper;

    public DentistAdapters(
            DentistJpaRepository dentistJpaRepository,
            UserIdentityJpaRepository userIdentityJpaRepository,
            DentistWriteEntityMapper writeEntityMapper,
            DentistReadEntityMapper readEntityMapper) {
        this.dentistJpaRepository = dentistJpaRepository;
        this.userIdentityJpaRepository = userIdentityJpaRepository;
        this.writeEntityMapper = writeEntityMapper;
        this.readEntityMapper = readEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dentist> findById(DentistId id) {
        if (id == null || id.value() == null) {
            return Optional.empty();
        }
        return dentistJpaRepository.findById(id.value())
                .map(readEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dentist> findAll(Pageable pageable) {
        return dentistJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Dentist save(Dentist dentist) {
        if (dentist == null) {
            return null;
        }

        DentistEntity entity = writeEntityMapper.toEntity(dentist);
        
        // Establecer relación con UserIdentity si existe
        if (dentist.getUserId() != null && dentist.getUserId().value() != null) {
            userIdentityJpaRepository.findById(dentist.getUserId().value())
                    .ifPresent(entity::setUserIdentity);
        }
        
        DentistEntity savedEntity = dentistJpaRepository.save(entity);
        return readEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dentist> findBySpecialty(String specialty, Pageable pageable) {
        return dentistJpaRepository.findBySpecialties(specialty, pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public void deleteById(DentistId id) {
        if (id != null && id.value() != null) {
            dentistJpaRepository.deleteById(id.value());
        }
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && dentistJpaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dentist> findByUserId(UserIdentityId userIdentityId) {
        if (userIdentityId == null || userIdentityId.value() == null) {
            return Optional.empty();
        }
        return dentistJpaRepository.findByUserIdentityId(userIdentityId.value())
                .map(readEntityMapper::toDomain);
    }
}