package com.example.ClinicaDefinitiva.infrastructure.repository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RateJpaRepository extends JpaRepository<RateEntity, String> {

    @Query("select r from RateEntity r where r.serviceCode = :serviceCode and r.contractId = :contractId and (r.validFrom is null or r.validFrom <= :now) and (r.validTo is null or r.validTo >= :now)")
    Optional<RateEntity> findActiveForService(@Param("serviceCode") String serviceCode,
                                              @Param("contractId") Long contractId,
                                              @Param("now") LocalDateTime now);
}

