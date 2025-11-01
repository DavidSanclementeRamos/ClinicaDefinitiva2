package com.example.ClinicaDefinitiva.infrastructure.repository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ContractJpaRepository extends JpaRepository<ContractEntity, Long> {
}
