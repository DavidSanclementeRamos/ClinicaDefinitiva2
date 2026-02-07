package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.authorization;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.authorization.RolEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolJpaRepository extends JpaRepository<RolEntity,Long> {
    Optional<RolEntity> findByRolEnum(String rolEnum);
    Page<RolEntity> findByIsEditable(boolean isEditable, Pageable pageable);
}
