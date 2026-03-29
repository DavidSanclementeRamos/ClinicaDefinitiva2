package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ShiftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShiftJpaRepository extends JpaRepository<ShiftEntity, Long> {

    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.date = :date " +
           "AND s.status = :status")
    Page<ShiftEntity> findActiveByDentistAndDate(
            @Param("dentistId") Long dentistId,
            @Param("date") LocalDate date,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.date = :date " +
           "AND s.status = :status")
    List<ShiftEntity> findActiveByDentistAndDate(
            @Param("dentistId") Long dentistId,
            @Param("date") LocalDate date,
            @Param("status") String status);

    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.date BETWEEN :startDate AND :endDate " +
           "AND s.status = :status")
    Page<ShiftEntity> findActiveByDentistAndDateRange(
            @Param("dentistId") Long dentistId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.date = :date " +
           "AND s.startTime < :endTime " +
           "AND s.endTime > :startTime")
    Page<ShiftEntity> findOverlapping(
            @Param("dentistId") Long dentistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.date = :date " +
           "AND s.startTime < :endTime " +
           "AND s.endTime > :startTime")
    Page<ShiftEntity> findOverlappingWithLock(
            @Param("dentistId") Long dentistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            Pageable pageable);

    @Query("SELECT s FROM ShiftEntity s " +
           "WHERE s.dentist.id = :dentistId " +
           "AND s.status = :status")
    Page<ShiftEntity> findActiveByDentist(
            @Param("dentistId") Long dentistId,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT s FROM ShiftEntity s " +
           "JOIN s.dentist d " +
           "WHERE d.id = :receptionistId") // Nota: Esto asume relación, puede necesitar ajuste
    List<ShiftEntity> findByReceptionistId(@Param("receptionistId") Long receptionistId);
}