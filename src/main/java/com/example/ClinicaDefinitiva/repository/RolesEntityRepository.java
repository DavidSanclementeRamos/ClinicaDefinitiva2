package com.example.ClinicaDefinitiva.repository;

import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesEntityRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByRoleEnum(Roles roles);
}
