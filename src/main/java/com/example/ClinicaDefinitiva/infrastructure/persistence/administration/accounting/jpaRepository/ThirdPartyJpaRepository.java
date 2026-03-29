package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ThirdPartyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThirdPartyJpaRepository extends JpaRepository<ThirdPartyEntity, Long> {
    
    Optional<ThirdPartyEntity> findByDocumentNumber(String documentNumber);
    
    @Query("SELECT t FROM ThirdPartyEntity t WHERE t.company.id = :companyId")
    Page<ThirdPartyEntity> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT t FROM ThirdPartyEntity t WHERE t.thirdPartyType = :type")
    Page<ThirdPartyEntity> findByThirdPartyType(@Param("type") String type, Pageable pageable);
    
    @Query("SELECT t FROM ThirdPartyEntity t WHERE t.thirdPartyType = :type AND t.active = true")
    Page<ThirdPartyEntity> findActiveByType(@Param("type") String type, Pageable pageable);
    
    boolean existsByDocumentNumber(String documentNumber);
}
