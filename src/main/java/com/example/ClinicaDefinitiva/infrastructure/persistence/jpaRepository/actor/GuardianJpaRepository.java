package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.GuardianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianJpaRepository extends JpaRepository<GuardianEntity, Long> {
}
