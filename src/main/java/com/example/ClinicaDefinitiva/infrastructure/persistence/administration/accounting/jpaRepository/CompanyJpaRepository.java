package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Long> {
    
    Optional<CompanyEntity> findByTaxId(String taxId);
    
    @Query("SELECT c FROM CompanyEntity c WHERE c.status = :status")
    Page<CompanyEntity> findByStatus(@Param("status") String status, Pageable pageable);
    
    @Query("SELECT c FROM CompanyEntity c WHERE c.taxRegime = :regime")
    Page<CompanyEntity> findByTaxRegime(@Param("regime") String regime, Pageable pageable);
    
    boolean existsByTaxId(String taxId);


}
