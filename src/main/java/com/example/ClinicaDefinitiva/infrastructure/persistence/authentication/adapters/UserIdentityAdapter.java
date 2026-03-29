package com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.adapters;

import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.entity.UserIdentityEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.mapper.UserReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.mapper.UserWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;




import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
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
    @Transactional(readOnly = true)
    public Optional<UserIdentity> findById(UserIdentityId id) {
        return userJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserIdentity> findAll(Pageable pageable) {
        return userJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserIdentity> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserIdentity> findByEmailAndStatus(String email, String status) {
        return userJpaRepository.findByEmailAndStatus(email, status)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserIdentity> findByIdAndStatus(UserIdentityId id, String status) {
        return userJpaRepository.findByIdAndStatus(id.value(), status)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional
    public UserIdentity save(UserIdentity user) {
        UserIdentityEntity entity = writeMapper.toEntity(user);
        UserIdentityEntity savedEntity = userJpaRepository.save(entity);
        UserIdentity savedUser = readMapper.toDomain(savedEntity);
        
     
      
        return savedUser;
    }
}