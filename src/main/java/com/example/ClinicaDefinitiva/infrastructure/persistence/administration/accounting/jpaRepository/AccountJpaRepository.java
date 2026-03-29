package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.LedgerAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<LedgerAccountEntity, Long> {
    
    Optional<LedgerAccountEntity> findByCode(String code);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE a.company.id = :companyId")
    Page<LedgerAccountEntity> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE a.nature = :nature")
    Page<LedgerAccountEntity> findByNature(@Param("nature") String nature, Pageable pageable);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE a.active = true")
    Page<LedgerAccountEntity> findActiveAccounts(Pageable pageable);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE a.code LIKE :parentCode% AND a.code != :parentCode")
Page<LedgerAccountEntity> findChildAccounts(@Param("parentCode") String parentCode, Pageable pageable);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE LENGTH(a.code) = :level")
    Page<LedgerAccountEntity> findByLevel(@Param("level") int level, Pageable pageable);
    
    @Query("SELECT a FROM LedgerAccountEntity a WHERE SUBSTRING(a.code, 1, 1) = :firstDigit")
Page<LedgerAccountEntity> findByFirstDigit(@Param("firstDigit") String firstDigit, Pageable pageable);
    
    boolean existsByCode(String code);
}
