package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.AdministrativeReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AdministrativeReportJpaRepository extends JpaRepository<AdministrativeReportEntity, Long> {
    
    @Query("SELECT r FROM AdministrativeReportEntity r " +
           "WHERE r.periodStart >= :start AND r.periodEnd <= :end")
    Page<AdministrativeReportEntity> findByPeriod(
            @Param("start") LocalDate start, 
            @Param("end") LocalDate end, 
            Pageable pageable);
    
    @Query("SELECT r FROM AdministrativeReportEntity r WHERE r.createdBy.id = :createdBy")
    Page<AdministrativeReportEntity> findByCreatedBy(
            @Param("createdBy") Long createdBy, 
            Pageable pageable);
    
    @Query("SELECT r FROM AdministrativeReportEntity r WHERE r.status = :status")
    Page<AdministrativeReportEntity> findByStatus(
            @Param("status") String status, 
            Pageable pageable);
    
    @Query("SELECT r FROM AdministrativeReportEntity r WHERE r.status = 'PUBLISHED'")
    Page<AdministrativeReportEntity> findPublishedReports(Pageable pageable);
    
    @Query("SELECT r FROM AdministrativeReportEntity r WHERE r.status = 'DRAFT'")
    Page<AdministrativeReportEntity> findDraftReports(Pageable pageable);
}
