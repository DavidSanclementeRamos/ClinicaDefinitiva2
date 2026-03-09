package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.administration.authorization;

import com.example.ClinicaDefinitiva.domain.administration.authorization.enu.RolEnum;
import com.example.ClinicaDefinitiva.domain.administration.authorization.model.Rol;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.RolRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authorization.RolJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.administration.authorization.RolEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class RolAdapter implements RolRepository {

    private final RolJpaRepository rolJpaRepository;
    private final RolEntityMapper rolEntityMapper;

    public RolAdapter(RolJpaRepository rolJpaRepository, RolEntityMapper rolEntityMapper) {
        this.rolJpaRepository = rolJpaRepository;
        this.rolEntityMapper = rolEntityMapper;
    }

    @Override
    public Optional<Rol> findById(RolId id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Rol> findByRolEnum(RolEnum rolEnum) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Rol> findByEditable(boolean editable, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Rol save(Rol rol) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(RolId id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsByDescription(String description) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   /** @Override
    public Optional<Rol> findById(RolId id) {
        return rolJpaRepository.findById(id.getValue())
                .map(rolEntityMapper::toDomain);
    }

    @Override
    public Optional<Rol> findByRolEnum(RolEnum rolEnum) {
        return rolJpaRepository.findByRolEnum(rolEnum.name())
                .map(rolEntityMapper::toDomain);
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        return rolJpaRepository.findAll(pageable)
                .map(rolEntityMapper::toDomain);
    }

    @Override
    public Page<Rol> findByEditable(boolean editable, Pageable pageable) {
        return rolJpaRepository.findByIsEditable(editable, pageable)
                .map(rolEntityMapper::toDomain);
    }

    @Override
    public Rol save(Rol rol) {
        var entity = rolEntityMapper.toEntity(rol);
        var saved = rolJpaRepository.save(entity);
        return rolEntityMapper.toDomain(saved);
    }

    @Override
    public void delete(RolId id) {
        rolJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByDescription(String description) {
        return false;
    }*/
}
