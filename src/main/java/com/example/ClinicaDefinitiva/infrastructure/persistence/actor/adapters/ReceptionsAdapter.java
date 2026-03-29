package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.ReceptionistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.ReceptionistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.reception.ReceptionReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.reception.ReceptionWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class ReceptionsAdapter implements ReceptionRepository {

    private final ReceptionistJpaRepository receptionistJpaRepository;
    private final UserIdentityJpaRepository userIdentityJpaRepository;
    private final ReceptionReadEntityMapper readMapper;
    private final ReceptionWriteEntityMapper writeMapper;

    public ReceptionsAdapter(
            ReceptionistJpaRepository receptionistJpaRepository,
            UserIdentityJpaRepository userIdentityJpaRepository,
            ReceptionReadEntityMapper readMapper,
            ReceptionWriteEntityMapper writeMapper) {
        this.receptionistJpaRepository = receptionistJpaRepository;
        this.userIdentityJpaRepository = userIdentityJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receptionist> findById(ReceptionId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return receptionistJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Receptionist> findAll(Pageable pageable) {
        return receptionistJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Receptionist save(Receptionist receptionist) {
        if (receptionist == null) {
            return null;
        }

        ReceptionistEntity entity = writeMapper.toEntity(receptionist);
        
        // Establecer relación con UserIdentity
        if (receptionist.getUserIdentityId() != null && receptionist.getUserIdentityId().value() != null) {
            userIdentityJpaRepository.findById(receptionist.getUserIdentityId().value())
                    .ifPresent(entity::setUserIdentity);
        }
        
        ReceptionistEntity savedEntity = receptionistJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    

    @Override
    public boolean existsById(ReceptionId id) {
        return id != null && id.getValue() != null && 
               receptionistJpaRepository.existsById(id.getValue());
    }

    @Override
    public void deleteById(ReceptionId id) {
        if (id != null && id.getValue() != null) {
            receptionistJpaRepository.deleteById(id.getValue());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Receptionist> findBySector(String sector, Pageable pageable) {
        if (sector == null || sector.trim().isEmpty()) {
            return Page.empty();
        }
        return receptionistJpaRepository.findBySector(sector, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receptionist> findByUserId(UserIdentityId userIdentityId) {
        if (userIdentityId == null || userIdentityId.value() == null) {
            return Optional.empty();
        }
        return receptionistJpaRepository.findByUserIdentity(userIdentityId.value())
                .map(readMapper::toDomain);
    }
}