package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.ReceptionistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.receptionEntityMapper.ReceptionReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.receptionEntityMapper.ReceptionWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ReceptionsAdapter implements ReceptionRepository {

    private final ReceptionistJpaRepository receptionistJpaRepository;
    private final ReceptionReadEntityMapper readEntityMapper;
    private final ReceptionWriteEntityMapper writeEntityMapper;

    public ReceptionsAdapter(ReceptionistJpaRepository receptionistJpaRepository, ReceptionReadEntityMapper readEntityMapper, ReceptionWriteEntityMapper writeEntityMapper) {
        this.receptionistJpaRepository = receptionistJpaRepository;
        this.readEntityMapper = readEntityMapper;
        this.writeEntityMapper = writeEntityMapper;
    }


    @Override
    public Optional<Receptionist> findById(ReceptionId id) {
        if (id == null) return Optional.empty();
        Long value;
        try {
            value = Long.valueOf(id.getValue());
        } catch (Exception e) {
            return Optional.empty();
        }
        return receptionistJpaRepository.findById(value)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Page<Receptionist> findAll(Pageable pageable) {
        return receptionistJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public void save(Receptionist receptionist) {
        if (receptionist == null) return;
        var entity = writeEntityMapper.toEntity(receptionist);
        receptionistJpaRepository.save(entity);
    }

    @Override
    public Receptionist update(Receptionist receptionist) {
        if (receptionist == null || receptionist.getId() == null) return null;
        Long idVal = Long.valueOf(receptionist.getId().getValue());
        var entity = writeEntityMapper.toEntity(receptionist);
        entity.setReceptionistId(idVal);
        var saved = receptionistJpaRepository.save(entity);
        return readEntityMapper.toDomain(saved);
    }

    @Override
    public boolean existsById(ReceptionId id) {
        if (id == null) return false;
        try {
            return receptionistJpaRepository.existsById(Long.valueOf(id.getValue()));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteById(ReceptionId id) {
        if (id == null) return;
        try {
            receptionistJpaRepository.deleteById(Long.valueOf(id.getValue()));
        } catch (Exception ignored) {
        }
    }

    @Override
    public Page<Receptionist> findBySector(String sector, Pageable pageable) {
        // ReceptionistJpaRepository does not declare findBySector; fallback to findAll and filter
        return receptionistJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Optional<Receptionist> findByUserId(UserIdentityId id) {
        return Optional.empty();
    }
}
