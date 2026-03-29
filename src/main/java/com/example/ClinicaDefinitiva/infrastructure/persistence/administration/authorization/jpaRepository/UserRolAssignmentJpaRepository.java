package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.UserRoleAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRolAssignmentJpaRepository extends JpaRepository<UserRoleAssignmentEntity, Long> {
    
    List<UserRoleAssignmentEntity> findByUserIdentityId(Long userId);
    
    Optional<UserRoleAssignmentEntity> findByUserIdentityIdAndIsPrimary(Long userId, boolean isPrimary);
    
    @Query("SELECT u FROM UserRoleAssignmentEntity u WHERE u.userIdentity.id = :userId AND u.role.id = :rolId")
    List<UserRoleAssignmentEntity> findByUserIdentityIdAndRolId(@Param("userId") Long userId, 
                                                                @Param("rolId") Long rolId);
    
    @Modifying
    @Query("UPDATE UserRoleAssignmentEntity u SET u.isPrimary = :isPrimary WHERE u.id = :id")
    void updatePrimary(@Param("id") Long id, @Param("isPrimary") boolean isPrimary);
}
