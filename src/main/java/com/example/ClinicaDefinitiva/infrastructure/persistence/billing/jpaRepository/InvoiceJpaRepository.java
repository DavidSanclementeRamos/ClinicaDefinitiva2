package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.updatedAt BETWEEN :start AND :end")
    Page<InvoiceEntity> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.dentist.id = :dentistId")
    Page<InvoiceEntity> findByDentistId(
            @Param("dentistId") Long dentistId,
            Pageable pageable);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.patient.id = :patientId")
    Page<InvoiceEntity> findByPatientId(
            @Param("patientId") Long patientId,
            Pageable pageable);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.status = :status")
    Page<InvoiceEntity> findByStatus(
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM InvoiceEntity i " +
           "JOIN i.items it WHERE it.dentalService.id = :serviceId")
    boolean existsByServiceId(@Param("serviceId") Long serviceId);
}
