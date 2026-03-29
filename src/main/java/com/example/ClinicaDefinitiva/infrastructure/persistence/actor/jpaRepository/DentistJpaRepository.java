package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistJpaRepository extends JpaRepository<DentistEntity,Long> {
    Page<DentistEntity> findBySpecialties(String specialty, Pageable pageable);
    
     @Query("SELECT d FROM DentistEntity d WHERE d.userIdentity.id = :userId")
    Optional<DentistEntity> findByUserIdentityId(@Param("userId") Long userId);
}
