package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.authorization.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface RolJpaRepository extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByRoleType(String roleType);
    
    @Query("SELECT r FROM RoleEntity r WHERE r.isEditable = :editable")
    Page<RoleEntity> findByIsEditable(@Param("editable") boolean editable, Pageable pageable);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RoleEntity r WHERE r.description = :description")
boolean existsByDescription(@Param("description") String description);
}
