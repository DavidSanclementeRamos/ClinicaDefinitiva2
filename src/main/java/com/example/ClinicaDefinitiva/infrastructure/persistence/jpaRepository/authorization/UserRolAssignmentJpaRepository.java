package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authorization;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization.UserRolAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRolAssignmentJpaRepository extends JpaRepository<UserRolAssignmentEntity, Long> {

    List<UserRolAssignmentEntity> findByUserId(Long userId );

    List<UserRolAssignmentEntity> findByUserIdAndRolId(Long userId, Long rolId);

    Optional<UserRolAssignmentEntity> findByUserIdAndPrimary(Long userId, boolean primary);

    @Modifying
    @Query("UPDATE UserRolAssignmentEntity u SET u.primary = :isPrimary WHERE u.id = :assignmentId")
    void updatePrimary(Long assignmentId, boolean isPrimary);
}
