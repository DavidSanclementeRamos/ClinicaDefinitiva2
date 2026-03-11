package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.PatientEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    
    @Query("SELECT p FROM PatientEntity p WHERE p.contractId = :contractId")
    Page<PatientEntity> findByContractId(@Param("contractId") String contractId, Pageable pageable);
    
    @Query("SELECT p FROM PatientEntity p WHERE p.guardian.id = :guardianId")
    Page<PatientEntity> findByGuardianId(@Param("guardianId") Long guardianId, Pageable pageable);
    
    @Query("SELECT p FROM PatientEntity p WHERE p.user = :userId")
    Optional<PatientEntity> findByUserId(@Param("userId") String userId);
}