package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.adapters;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.RoleEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.jpaRepository.RolJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.rol.RoleReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.rol.RoleWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RolAdapter implements RolRepository {

    private final RolJpaRepository rolJpaRepository;
    private final RoleReadEntityMapper readMapper;
    private final RoleWriteEntityMapper writeMapper;

    public RolAdapter(RolJpaRepository rolJpaRepository, 
                     RoleReadEntityMapper readMapper,
                     RoleWriteEntityMapper writeMapper) {
        this.rolJpaRepository = rolJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findById(RolId id) {
        return rolJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rol> findByRolEnum(RolEnum rolEnum) {
        return rolJpaRepository.findByRoleType(rolEnum.name())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rol> findAll(Pageable pageable) {
        return rolJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rol> findByEditable(boolean editable, Pageable pageable) {
        return rolJpaRepository.findByIsEditable(editable, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional
    public Rol save(Rol rol) {
        RoleEntity entity = writeMapper.toEntity(rol);
        RoleEntity savedEntity = rolJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void delete(RolId id) {
        rolJpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDescription(String description) {
        return rolJpaRepository.existsByDescription(description);
    }
}