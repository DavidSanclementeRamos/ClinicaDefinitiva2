package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.GuardianEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardianJpaRepository extends JpaRepository<GuardianEntity, Long> {
    
     @Query("SELECT g FROM GuardianEntity g JOIN PatientEntity p ON g.id = p.guardian.id WHERE p.id = :patientId")
    Page<GuardianEntity> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);
    
    @Query("SELECT g FROM GuardianEntity g WHERE g.userIdentity.id = :userId")
    Optional<GuardianEntity> findByUserIdentityId(@Param("userId") Long userId);
}
