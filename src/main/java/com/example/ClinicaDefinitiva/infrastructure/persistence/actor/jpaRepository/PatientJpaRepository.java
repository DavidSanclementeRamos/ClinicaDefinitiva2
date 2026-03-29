package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PatientEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    
    @Query("SELECT p FROM PatientEntity p WHERE p.contract.id = :contractId")
    Page<PatientEntity> findByContractId(@Param("contractId") Long contractId, Pageable pageable);
    
    @Query("SELECT p FROM PatientEntity p WHERE p.guardian.id = :guardianId")
    Page<PatientEntity> findByGuardianId(@Param("guardianId") Long guardianId, Pageable pageable);
    
    @Query("SELECT p FROM PatientEntity p WHERE p.userIdentity.id = :userId")
    Optional<PatientEntity> findByUserIdentityId(@Param("userId") Long userId);
}