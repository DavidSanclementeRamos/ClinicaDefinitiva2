package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.authorization;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.output.UserRolAssignmentRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authorization.UserRolAssignmentJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.authorization.UserRolAssignmentEntityMapper;

import java.util.List;
import java.util.Optional;

public class UserRolAssignmentAdapter implements UserRolAssignmentRepository {

    private final UserRolAssignmentJpaRepository jpaRepository;
    private final UserRolAssignmentEntityMapper mapper;

    public UserRolAssignmentAdapter(UserRolAssignmentJpaRepository jpaRepository,
                                    UserRolAssignmentEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public UserRolAssignment save(UserRolAssignment assignment) {
        var entity = mapper.toEntity(assignment);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserRolAssignment> findById(UserRolAssignmentId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }


    @Override
    public Optional<UserRolAssignment> findByUserIdAndIsPrimary(UserId userId, boolean isPrimary) {
        return jpaRepository.findByUserIdAndPrimary(userId.getValue(), isPrimary)
                .map(mapper::toDomain);
    }

    @Override
    public void updatePrimary(UserRolAssignmentId assignmentId, boolean isPrimary) {
        jpaRepository.updatePrimary(assignmentId.getValue(), isPrimary);
    }

    @Override
    public void delete(UserRolAssignmentId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public List<UserRolAssignment> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.getValue())
                .stream().map(mapper::toDomain).toList();
    }


    @Override
    public List<UserRolAssignment> findByUserIdAndRolId(UserId userId, RolId rolId) {
        return  jpaRepository.findByUserIdAndRolId(userId.getValue(), rolId.getValue())
                .stream().map(mapper::toDomain).toList(); }
}
