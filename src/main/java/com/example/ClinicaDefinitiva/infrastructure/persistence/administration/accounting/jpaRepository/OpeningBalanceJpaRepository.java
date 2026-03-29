package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.OpeningBalanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OpeningBalanceJpaRepository extends JpaRepository<OpeningBalanceEntity, Long> {
    
    @Query("SELECT o FROM OpeningBalanceEntity o WHERE o.company.id = :companyId")
    Page<OpeningBalanceEntity> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT o FROM OpeningBalanceEntity o WHERE o.account.id = :accountId")
    Page<OpeningBalanceEntity> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);
    
    @Query("SELECT o FROM OpeningBalanceEntity o WHERE o.date = :date")
    Page<OpeningBalanceEntity> findByDate(@Param("date") LocalDate date, Pageable pageable);
    
    @Query("SELECT o FROM OpeningBalanceEntity o WHERE o.account.id = :accountId AND o.date = :date")
    Optional<OpeningBalanceEntity> findByAccountAndDate(
            @Param("accountId") Long accountId, 
            @Param("date") LocalDate date);
}
