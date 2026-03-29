package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.adapters;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.UserRoleAssignmentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.jpaRepository.RolJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.jpaRepository.UserRolAssignmentJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.userRolAssignment.UserRoleAssignmentReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.mapper.userRolAssignment.UserRoleAssignmentWriteEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.authentication.jpaRepository.UserIdentityJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRolAssignmentAdapter implements UserRolAssignmentRepository {

    private final UserRolAssignmentJpaRepository jpaRepository;
    private final UserIdentityJpaRepository userJpaRepository;
    private final RolJpaRepository rolJpaRepository;
    private final UserRoleAssignmentReadEntityMapper readMapper;
    private final UserRoleAssignmentWriteEntityMapper writeMapper;

    public UserRolAssignmentAdapter(
            UserRolAssignmentJpaRepository jpaRepository,
            UserIdentityJpaRepository userJpaRepository,
            RolJpaRepository rolJpaRepository,
            UserRoleAssignmentReadEntityMapper readMapper,
            UserRoleAssignmentWriteEntityMapper writeMapper) {
        this.jpaRepository = jpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.rolJpaRepository = rolJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional
    public UserRolAssignment save(UserRolAssignment assignment) {
        UserRoleAssignmentEntity entity = writeMapper.toEntity(assignment);
        
        // Establecer relaciones
        if (assignment.getUserId() != null) {
            userJpaRepository.findById(assignment.getUserId().value())
                    .ifPresent(entity::setUserIdentity);
        }
        
        if (assignment.getRolId() != null) {
            rolJpaRepository.findById(assignment.getRolId().getValue())
                    .ifPresent(entity::setRole);
        }
        
        UserRoleAssignmentEntity savedEntity = jpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRolAssignment> findById(UserRolAssignmentId id) {
        return jpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserRolAssignment> findByUserIdAndIsPrimary(UserIdentityId userIdentityId, boolean isPrimary) {
        return jpaRepository.findByUserIdentityIdAndIsPrimary(userIdentityId.value(), isPrimary)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional
    public void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary) {
        jpaRepository.updatePrimary(assignmentId.getValue(), isPrimary);
    }

    @Override
    @Transactional
    public void delete(UserRolAssignmentId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRolAssignment> findByUserId(UserIdentityId userIdentityId) {
        return jpaRepository.findByUserIdentityId(userIdentityId.value())
                .stream()
                .map(readMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRolAssignment> findByUserIdAndRolId(UserIdentityId userIdentityId, RolId rolId) {
        return jpaRepository.findByUserIdentityIdAndRolId(userIdentityId.value(), rolId.getValue())
                .stream()
                .map(readMapper::toDomain)
                .collect(Collectors.toList());
    }
}
