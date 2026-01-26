package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistJpaRepository extends JpaRepository<ReceptionistEntity, Long> {
}
