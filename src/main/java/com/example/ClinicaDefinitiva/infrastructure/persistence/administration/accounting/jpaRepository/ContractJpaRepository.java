package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContractJpaRepository extends JpaRepository<ContractEntity, Long> {
    
    @Query("SELECT c FROM ContractEntity c WHERE c.company.id = :companyId")
    Page<ContractEntity> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT c FROM ContractEntity c WHERE c.thirdParty.id = :thirdPartyId")
    Page<ContractEntity> findByThirdPartyId(@Param("thirdPartyId") Long thirdPartyId, Pageable pageable);
    
    @Query("SELECT c FROM ContractEntity c WHERE c.status = :status")
    Page<ContractEntity> findByStatus(@Param("status") String status, Pageable pageable);
    
    @Query("SELECT c FROM ContractEntity c WHERE c.status = 'ACTIVE'")
    Page<ContractEntity> findActiveContracts(Pageable pageable);
    
    @Query("SELECT c FROM ContractEntity c WHERE c.endDate <= :cutoff AND c.status != 'EXPIRED'")
    Page<ContractEntity> findExpiringBefore(@Param("cutoff") LocalDate cutoff, Pageable pageable);
    
    @Query("SELECT c FROM ContractEntity c WHERE c.endDate < :date AND c.status = 'ACTIVE'")
    Page<ContractEntity> findExpiredActive(@Param("date") LocalDate date, Pageable pageable);
    
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ContractEntity c " +
           "WHERE c.thirdParty.id = :thirdPartyId AND c.status = 'ACTIVE'")
    boolean existsActiveForThirdParty(@Param("thirdPartyId") Long thirdPartyId);
}
