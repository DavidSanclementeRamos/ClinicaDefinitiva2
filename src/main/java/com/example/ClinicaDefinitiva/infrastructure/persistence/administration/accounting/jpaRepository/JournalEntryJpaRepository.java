package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JournalEntryJpaRepository extends JpaRepository<JournalEntryEntity, Long> {
    
    @Query("SELECT e FROM JournalEntryEntity e WHERE e.company.id = :companyId")
    Page<JournalEntryEntity> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT e FROM JournalEntryEntity e WHERE e.date BETWEEN :start AND :end")
    Page<JournalEntryEntity> findByDateRange(
            @Param("start") LocalDate start, 
            @Param("end") LocalDate end, 
            Pageable pageable);
    
    @Query("SELECT e FROM JournalEntryEntity e JOIN e.lines l WHERE l.account.id = :accountId")
    Page<JournalEntryEntity> findByAccountId(
            @Param("accountId") Long accountId, 
            Pageable pageable);
    
    @Query("SELECT e FROM JournalEntryEntity e JOIN e.lines l WHERE l.thirdParty.id = :thirdPartyId")
    Page<JournalEntryEntity> findByThirdPartyId(
            @Param("thirdPartyId") Long thirdPartyId, 
            Pageable pageable);
    
    @Query("SELECT e FROM JournalEntryEntity e WHERE e.posted = false")
    Page<JournalEntryEntity> findUnpostedEntries(Pageable pageable);
    
    Optional<JournalEntryEntity> findByDocumentNumber(String documentNumber);
    
    boolean existsByDocumentNumber(String documentNumber);
}
