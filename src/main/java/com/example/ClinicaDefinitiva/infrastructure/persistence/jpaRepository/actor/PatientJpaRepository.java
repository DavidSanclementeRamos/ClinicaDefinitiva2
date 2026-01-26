package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
}
