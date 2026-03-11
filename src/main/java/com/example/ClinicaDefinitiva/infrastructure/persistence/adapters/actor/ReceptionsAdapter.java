package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.ReceptionistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.receptionEntityMapper.ReceptionReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.receptionEntityMapper.ReceptionWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ReceptionsAdapter implements ReceptionRepository {

    private final ReceptionistJpaRepository receptionistJpaRepository;
    private final ReceptionReadEntityMapper readEntityMapper;
    private final ReceptionWriteEntityMapper writeEntityMapper;

    public ReceptionsAdapter(
            ReceptionistJpaRepository receptionistJpaRepository,
            ReceptionReadEntityMapper readEntityMapper,
            ReceptionWriteEntityMapper writeEntityMapper) {
        this.receptionistJpaRepository = receptionistJpaRepository;
        this.readEntityMapper = readEntityMapper;
        this.writeEntityMapper = writeEntityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receptionist> findById(ReceptionId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return receptionistJpaRepository.findById(id.getValue())
                .map(readEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Receptionist> findAll(Pageable pageable) {
        return receptionistJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public Receptionist save(Receptionist receptionist) {
        if (receptionist == null) {
            return null;
        }
        ReceptionistEntity entity = writeEntityMapper.toEntity(receptionist);
        ReceptionistEntity savedEntity = receptionistJpaRepository.save(entity);
        return readEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Receptionist update(Receptionist receptionist) {
        if (receptionist == null || receptionist.getId() == null) {
            return null;
        }
        
        // Verificar que existe
        if (!receptionistJpaRepository.existsById(receptionist.getId().getValue())) {
            return null;
        }
        
        ReceptionistEntity entity = writeEntityMapper.toEntity(receptionist);
        entity.setReceptionistId(receptionist.getId().getValue());
        ReceptionistEntity savedEntity = receptionistJpaRepository.save(entity);
        return readEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ReceptionId id) {
        if (id == null || id.getValue() == null) {
            return false;
        }
        return receptionistJpaRepository.existsById(id.getValue());
    }

    @Override
    @Transactional
    public void deleteById(ReceptionId id) {
        if (id != null && id.getValue() != null) {
            receptionistJpaRepository.deleteById(id.getValue());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Receptionist> findBySector(String sector, Pageable pageable) {
        return receptionistJpaRepository.findBySector(sector, pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Receptionist> findByUserId(UserIdentityId id) {
       
        
        Page<ReceptionistEntity> page = receptionistJpaRepository.findByUserId(
            id.value().toString(), 
            Pageable.ofSize(1)
        );
        
        return page.stream()
                .findFirst()
                .map(readEntityMapper::toDomain);
    }
}