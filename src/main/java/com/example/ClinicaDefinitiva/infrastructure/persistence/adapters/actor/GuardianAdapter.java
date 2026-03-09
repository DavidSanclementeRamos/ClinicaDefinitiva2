package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.GuardianJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.guardian.GuardianReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.guardian.GuardianWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class GuardianAdapter implements GuardianRepository {

    private final GuardianJpaRepository guardianJpaRepository;
    private final GuardianReadEntityMapper readEntityMapper;
    private final GuardianWriteEntityMapper writeEntityMapper;

    public GuardianAdapter(GuardianJpaRepository guardianJpaRepository, GuardianReadEntityMapper readEntityMapper, GuardianWriteEntityMapper writeEntityMapper) {
        this.guardianJpaRepository = guardianJpaRepository;
        this.readEntityMapper = readEntityMapper;
        this.writeEntityMapper = writeEntityMapper;
    }

    @Override
    public Optional<Guardian> findById(GuardianId guardianId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Guardian> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Guardian save(Guardian guardian) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Guardian update(GuardianId guardianId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Guardian deleteById(GuardianId guardianId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Guardian> findByPatientId(PatientId patientId, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsById(GuardianId guardianId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Guardian> findByUserId(UserIdentityId id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


  /**  @Override
    public Optional<Guardian> findById(GuardianId guardianId) {
        if (guardianId == null) return Optional.empty();
        try {
            Long val = guardianId.value();
            return guardianJpaRepository.findById(val).map(readEntityMapper::toDomain);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<Guardian> findAll(Pageable pageable) {
        return guardianJpaRepository.findAll(pageable).map(readEntityMapper::toDomain);
    }

    @Override
    public Guardian save(Guardian guardian) {
        if (guardian == null) return guardian;
        var entity = writeEntityMapper.toEntity(guardian);
        guardianJpaRepository.save(entity);
        return guardian;
    }

    @Override
    public Guardian update(GuardianId guardianId) {
        if (guardianId == null) return null;
        try {
            Long val = guardianId.value();
            var entity = guardianJpaRepository.findById(val).orElse(null);
            if (entity == null) return null;
            return readEntityMapper.toDomain(guardianJpaRepository.save(entity));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Guardian deleteById(GuardianId guardianId) {
        if (guardianId == null) return null;
        try {
            Long val = guardianId.value();
            var entity = guardianJpaRepository.findById(val).orElse(null);
            if (entity == null) return null;
            guardianJpaRepository.deleteById(val);
            return readEntityMapper.toDomain(entity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Page<Guardian> findByPatientId(PatientId patientId, Pageable pageable) {
        return null;
    }

   /** @Override
    public Page<Guardian> findByPatientId(Long patientId, Pageable pageable) {
        if (patientId == null) return Optional.empty();
        return guardianJpaRepository.findAll(pageable).stream()
                .map(readEntityMapper::toDomain)
                .filter(g -> {
                    if (g.getPatientList() != null) {
                        g.getPatientList().stream().anyMatch(p -> false);
                    }
                    return false;
                })
                .findFirst();
    }

    @Override
    public boolean existsById(GuardianId guardianId) {
        if (guardianId == null) return false;
        try {
            return guardianJpaRepository.existsById(guardianId.value());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Guardian> findByUserId(UserIdentityId id) {
        return null;
    }*/

}
