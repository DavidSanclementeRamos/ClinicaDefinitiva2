package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.DentistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DentistJpaRepository extends JpaRepository<DentistEntity,Long> {
   // Page<DentistEntity> findByAvailability(String status, Pageable pageable);
    Page<DentistEntity> findBySpecialties(String specialty, Pageable pageable);
}
