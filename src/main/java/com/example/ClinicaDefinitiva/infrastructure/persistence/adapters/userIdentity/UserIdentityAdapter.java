package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.userIdentity;

import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authentication.UserIdentityEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.userIdentity.UserIdentityJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.userIdentity.UserReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.userIdentity.UserWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public class UserIdentityAdapter implements UserIdentityRepository {
    private final UserIdentityJpaRepository userJpaRepository;
    private final UserReadEntityMapper readMapper;
    private final UserWriteEntityMapper writeMapper;

    public UserIdentityAdapter(UserIdentityJpaRepository userJpaRepository,
                               UserReadEntityMapper readMapper,
                               UserWriteEntityMapper writeMapper) {
        this.userJpaRepository = userJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public Optional<UserIdentity> findById(UserIdentityId id) {
        return userJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    public Page<UserIdentity> findAll(Pageable pageable) {
        return userJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Page<UserIdentity> findByEmail(String email, Pageable pageable) {
        return userJpaRepository.findByEmail(email, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Page<UserIdentity> findByEmailAndStatus(String email, String status, Pageable pageable) {
        return userJpaRepository.findByEmailAndStatus(email, status, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Page<UserIdentity> findByIdAndStatus(Long id, String status, Pageable pageable) {
        return userJpaRepository.findByIdAndStatus(id, status, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public UserIdentity save(UserIdentity user) {
        UserIdentityEntity entity = writeMapper.toEntity(user);
        UserIdentityEntity saved = userJpaRepository.save(entity);
        return readMapper.toDomain(saved);
    }
}
