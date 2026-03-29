package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.RateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RateJpaRepository extends JpaRepository<RateEntity, Long> {

    @Query("SELECT r FROM RateEntity r " +
           "WHERE r.dentalService.id = :serviceId " +
           "AND (r.contract.id = :contractId OR (r.contract.id IS NULL AND :contractId IS NULL)) " +
           "AND r.status = 'ACTIVE' " +
           "AND r.validFrom <= :now " +
           "AND (r.validUntil IS NULL OR r.validUntil >= :now)")
    Optional<RateEntity> findActiveRateForService(
            @Param("serviceId") Long serviceId,
            @Param("contractId") Long contractId,
            @Param("now") LocalDateTime now);

    @Query("SELECT r FROM RateEntity r " +
           "WHERE r.status = 'ACTIVE' " +
           "AND r.validFrom <= :now " +
           "AND (r.validUntil IS NULL OR r.validUntil >= :now)")
    Page<RateEntity> findCurrentlyValid(
            @Param("now") LocalDateTime now,
            Pageable pageable);

    @Query("SELECT r FROM RateEntity r WHERE r.contract.id = :contractId")
    Page<RateEntity> findByContractId(
            @Param("contractId") Long contractId,
            Pageable pageable);

    @Query("SELECT r FROM RateEntity r WHERE r.dentalService.id = :serviceId")
    Page<RateEntity> findByServiceId(
            @Param("serviceId") Long serviceId,
            Pageable pageable);

    @Query("SELECT r FROM RateEntity r WHERE r.payerType = :payerType")
    Page<RateEntity> findByPayerType(
            @Param("payerType") String payerType,
            Pageable pageable);
}