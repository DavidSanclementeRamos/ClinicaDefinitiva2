package com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.jpaRepository;


import com.example.ClinicaDefinitiva.infrastructure.persistence.schedule.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {

    // Consultas básicas
    Page<AppointmentEntity> findByDentistId(Long dentistId, Pageable pageable);
    
    Page<AppointmentEntity> findByPatientId(Long patientId, Pageable pageable);
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.dentalService.id = :serviceId")
    Page<AppointmentEntity> findByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);
    
    @Query("SELECT a FROM AppointmentEntity a WHERE a.status = :status")
    Page<AppointmentEntity> findByStatus(@Param("status") String status, Pageable pageable);

    // Consultas de conflictos con y sin lock
    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.startDateTime < :end " +
           "AND a.endDateTime > :start")
    List<AppointmentEntity> findConflictingForDentist(
            @Param("dentistId") Long dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.startDateTime < :end " +
           "AND a.endDateTime > :start")
    List<AppointmentEntity> findConflictingForDentistWithLock(
            @Param("dentistId") Long dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.startDateTime < :end " +
           "AND a.endDateTime > :start")
    List<AppointmentEntity> findConflictingForPatient(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.startDateTime < :end " +
           "AND a.endDateTime > :start")
    List<AppointmentEntity> findConflictingForPatientWithLock(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Consultas por rango de fechas
    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.startDateTime BETWEEN :start AND :end")
    Page<AppointmentEntity> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.startDateTime BETWEEN :start AND :end")
    Page<AppointmentEntity> findByDentistBetween(
            @Param("dentistId") Long dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.startDateTime BETWEEN :start AND :end")
    Page<AppointmentEntity> findByPatientBetween(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.dentist.id = :dentistId " +
           "AND a.startDateTime BETWEEN :start AND :end")
    Page<AppointmentEntity> findByPatientAndDentist(
            @Param("patientId") Long patientId,
            @Param("dentistId") Long dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);

    // Consultas para citas futuras/próximas
    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.startDateTime BETWEEN :now AND :limit " +
           "AND a.status = 'SCHEDULED'")
    Page<AppointmentEntity> findUpcomingForDentist(
            @Param("dentistId") Long dentistId,
            @Param("now") LocalDateTime now,
            @Param("limit") LocalDateTime limit,
            Pageable pageable);

    @Query("SELECT a FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.startDateTime > :now " +
           "AND a.status = 'SCHEDULED'")
    Page<AppointmentEntity> findFutureForPatient(
            @Param("patientId") Long patientId,
            @Param("now") LocalDateTime now,
            Pageable pageable);

    // Consultas de existencia (optimizadas)
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.startDateTime BETWEEN :start AND :end " +
           "AND a.status = :status")
    boolean existsScheduledByDentistBetween(
            @Param("dentistId") Long dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") String status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
           "WHERE a.patient.id = :patientId " +
           "AND a.startDateTime BETWEEN :start AND :end " +
           "AND a.status = :status")
    boolean existsScheduledByPatientBetween(
            @Param("patientId") Long patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") String status);

    @Query("SELECT COUNT(a) FROM AppointmentEntity a " +
           "WHERE a.dentist.id = :dentistId " +
           "AND a.status = :status")
    long countScheduledByDentist(
            @Param("dentistId") Long dentistId,
            @Param("status") String status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
           "WHERE a.dentalService.id = :serviceId")
    boolean existsByServiceId(@Param("serviceId") Long serviceId);
}