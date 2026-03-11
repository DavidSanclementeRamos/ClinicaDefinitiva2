package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.actor.ReceptionistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistJpaRepository extends JpaRepository<ReceptionistEntity, Long> {
    
    @Query("SELECT r FROM ReceptionistEntity r WHERE r.sector = :sector")
    Page<ReceptionistEntity> findBySector(@Param("sector") String sector, Pageable pageable);
    
    @Query("SELECT r FROM ReceptionistEntity r WHERE r.user = :userId")
    Page<ReceptionistEntity> findByUserId(@Param("userId") String userId, Pageable pageable);
}
